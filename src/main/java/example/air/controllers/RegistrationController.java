package example.air.controllers;

import example.air.entity.Roles;
import example.air.entity.Users;
import example.air.repositores.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Map<String,Object> model) {
        Users users = userRepository.findByUsername(user.getUsername());
        if(users!=null){
            model.put("message","User exists");
            return "registration";
        }else{
            user.setActive(true);
            user.setRoles(Collections.singleton(Roles.USER));
            userRepository.save(user);
            return "redirect:/login";
        }
    }
}
