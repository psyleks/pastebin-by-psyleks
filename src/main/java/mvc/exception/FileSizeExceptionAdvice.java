package mvc.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileSizeExceptionAdvice {
    @ExceptionHandler({MaxUploadSizeExceededException.class})
  public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ex.getMessage();
    }
}
