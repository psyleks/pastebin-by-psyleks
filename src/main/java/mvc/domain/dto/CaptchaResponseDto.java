package mvc.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {

    private boolean success;
    private List<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "CaptchaResponseDto{" +
                "success=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}

