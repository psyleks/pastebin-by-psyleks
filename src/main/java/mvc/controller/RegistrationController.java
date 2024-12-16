package mvc.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import mvc.domain.User;
import mvc.domain.dto.CaptchaResponseDto;
import mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${captchaSecret}")
    public String captchaSecret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("cf-turnstile-response") String recaptchaResponse,
            @Valid User user,  // Assuming a User entity class
            BindingResult bindingResult,
            Model model) {

        boolean invalidCaptcha = !validateCaptcha(recaptchaResponse);
        // Step 1: Validate CAPTCHA
        if (invalidCaptcha) {
            model.addAttribute("captchaError", "Captcha validation failed. Please try again.");
        }

        // Step 2: Validate password confirmation
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords do not match");
        }

        // Step 3: Handle binding result errors
        if (isConfirmEmpty || bindingResult.hasErrors() || invalidCaptcha) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";  // Show form again with validation errors
        }

        // Step 4: Check if the username already exists
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User already exists.");
            return "registration";  // Show registration form again if user exists
        }

        // Step 5: Registration successful, redirect to login
        return "redirect:/login";  // Redirect to login page after successful registration
    }

    @PostMapping("/register")
    private boolean validateCaptcha(String recaptchaResponse) {
        String url = UriComponentsBuilder.fromHttpUrl("https://challenges.cloudflare.com/turnstile/v0/siteverify")
                .queryParam("secret", captchaSecret)
                .queryParam("response", recaptchaResponse)
                .build().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", captchaSecret);
        map.add("response", recaptchaResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<CaptchaResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, CaptchaResponseDto.class);
        CaptchaResponseDto responseBody = response.getBody();

        try {

            if (responseBody != null) {
                System.out.println("Captcha response: " + responseBody);
                if (!responseBody.isSuccess()) {
                    // Логируем код ошибки, если он есть
                    if (responseBody.getErrorCodes() != null) {
                        System.out.println("Captcha error codes: " + responseBody.getErrorCodes());
                    } else {
                        System.out.println("No error codes in the response, but success=false");
                    }
                }
            } else {
                System.out.println("Received empty response from Turnstile API.");
            }

            return responseBody != null && responseBody.isSuccess();
        } catch (Exception e) {
            // Логируем ошибку
            e.printStackTrace();
            return false;
        }
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
