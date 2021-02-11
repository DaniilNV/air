package example.air.service;

import example.air.entity.Roles;
import example.air.entity.Users;
import example.air.repositores.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public int addUser(Users user) {
        Users user_1 = userRepository.findByUsername(user.getUsername());
        Users user_2 = userRepository.findByEmail(user.getEmail());
        if (user_1!=null && user_2!=null ) {
            return 0;
        }
        if (user_1!=null ) {
            return 1;
        }
        if (user_2!=null ) {
            return 2;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Roles.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sendMessage(user);
        userRepository.save(user);
        return 3;
    }

    private void sendMessage(Users user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n Welcome to Air. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailService.send(user.getEmail(), "Activation Code", message);
        }
    }

    public boolean activateUser(String code) {
        Users user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    public List<Users> findAll() {
        return userRepository.findAllByActiveIsTrue();
    }

    public void saveUser(Users user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Roles.values()).map(Roles::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Roles.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public void updateProfile(Users user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail));
        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            user.setActive(false);
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    public void subscribe(Users currentUser, Users user) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    public void unsubscribe(Users currentUser, Users user) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }
}
