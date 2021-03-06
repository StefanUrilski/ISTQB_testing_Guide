package com.example.exam.web.controllers;

import com.example.exam.domain.models.binding.UserRegisterBindingModel;
import com.example.exam.domain.models.service.UserServiceModel;
import com.example.exam.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@PreAuthorize("isAnonymous()")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("model") UserRegisterBindingModel userRegisterBindingModel) {
        return view("users/register-user", "model", userRegisterBindingModel);
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("model") UserRegisterBindingModel model,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors() || !(model.getPassword().equals(model.getConfirmPassword()))) {
            model.setPassword(null);
            model.setConfirmPassword(null);
            return view("users/register-user", "model", model);
        }

        UserServiceModel userServiceModel = modelMapper.map(model, UserServiceModel.class);
        userService.registerUser(userServiceModel);

        return redirect("/users/login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return view("users/login-user");
    }

}
