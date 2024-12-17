package mvc.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import mvc.domain.User;
import mvc.service.UserService;
import mvc.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("cf-turnstile-response") String recaptchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean invalidCaptcha = !captchaService.validateCaptcha(recaptchaResponse);
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        if (invalidCaptcha) {
            model.addAttribute("captchaError", "Captcha validation failed. Please try again.");
        }

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords do not match");
        }

        if (isConfirmEmpty || bindingResult.hasErrors() || invalidCaptcha) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User already exists.");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code, Model model) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation failed");
        }

        return "/login";
    }

}
