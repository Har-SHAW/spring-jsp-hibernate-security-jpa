package com.project.ecommerce.controller;

import com.project.ecommerce.binder.InitBinderClass;
import com.project.ecommerce.dto.user.UserSignup;
import com.project.ecommerce.entity.user.RolesEntity;
import com.project.ecommerce.entity.user.UserDetailsEntity;
import com.project.ecommerce.entity.user.UserEntity;
import com.project.ecommerce.repository.RoleRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class AuthController extends InitBinderClass {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public void registerTheUser(UserSignup theUser){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(encoder.encode(theUser.getPassword()));
        userEntity.setUsername(theUser.getUsername());

        UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setAge(theUser.getAge());
        userDetailsEntity.setEmail(theUser.getEmail());
        userDetailsEntity.setPhoneNo(theUser.getPhoneNo());

        userEntity.setUserDetailsEntity(userDetailsEntity);

        RolesEntity rolesEntity = roleRepository.getOne("ROLE_USER");

        userEntity.addRole(rolesEntity);

        userRepository.save(userEntity);
    }

    @RequestMapping("/signup")
    public String registerUser(Model model){
        model.addAttribute("user", new UserSignup());
        return "sign-up";
    }

    @PostMapping("/processSignup")
    public String processSignUp(@Valid @ModelAttribute("user") UserSignup theUser, BindingResult bindingResult, Model model){

        if (theUser.getConfirmPassword()!=null && !theUser.getPassword().equals(theUser.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Password not matched");
        }

        if (bindingResult.hasErrors()){
            return "sign-up";
        }else {
            if(userRepository.existsById(theUser.getUsername())) {
                return "error-page";
            }

            registerTheUser(theUser);

            return "log-in";
        }
    }
}
