package example.air.controllers;

import example.air.entity.Roles;
import example.air.entity.Users;
import example.air.repositores.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable Users user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Roles.values());
        return "userEdit";
    }

    @PostMapping()
    public String userSave(@RequestParam("userId") Users user, @RequestParam Map<String, String> form, @RequestParam String username) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Roles.values()).map(Roles::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()){
            if(roles.contains(key)){
                user.getRoles().add(Roles.valueOf(key));
            }
        }
        userRepository.save(user);
        return "redirect:/user";
    }
}
