package mvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/verify")
public class TurnstileController {

    @Value("${captchaSecret}")
    private static String SECRET_KEY;

    @PostMapping
    public ResponseEntity<String> handlePost(
            @RequestParam("cf-turnstile-response") String token,
            @RequestHeader("CF-Connecting-IP") String ip) {

        // Prepare data for Cloudflare validation API
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", SECRET_KEY);
        formData.add("response", token);
        formData.add("remoteip", ip);

        try {
            // Use RestTemplate to send the POST request to Cloudflare's siteverify endpoint
            RestTemplate restTemplate = new RestTemplate();
            String verifyUrl = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

            // Wrap the form data into an HttpEntity with headers (if needed)
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

            // Send the request and get the response
            ResponseEntity<String> result = restTemplate.postForEntity(verifyUrl, entity, String.class);

            // Parse the result from the response body (it's usually JSON)
            String outcome = result.getBody();
            if (outcome != null && outcome.contains("\"success\":false")) {
                return ResponseEntity.status(400).body("The provided Turnstile token was not valid! \n" + outcome);
            }

            // The Turnstile token was successfully validated
            return ResponseEntity.ok("Turnstile token successfully validated. \n" + outcome);

        } catch (Exception e) {
            // In case of an error, return a generic message
            return ResponseEntity.status(500).body("Error occurred during token verification. Please try again later.");
        }
    }
}
