package mvc.service;

import mvc.domain.dto.CaptchaResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaService {

    @Value("${captchaSecret}")
    public String captchaSecret;

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateCaptcha(String recaptchaResponse) {
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", captchaSecret);
        map.add("response", recaptchaResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<CaptchaResponseDto> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, CaptchaResponseDto.class);

            CaptchaResponseDto responseBody = response.getBody();
            return responseBody != null && responseBody.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }
}
