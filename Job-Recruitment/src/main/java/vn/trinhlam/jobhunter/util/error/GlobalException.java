package vn.trinhlam.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.val;
import vn.trinhlam.jobhunter.domain.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
    // can thiệp vào các exception xảy ra trong controller
    @ExceptionHandler(value = { UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class })
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exeption occurs...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { NoResourceFoundException.class, })

    public ResponseEntity<RestResponse<Object>> handleNotFountException(Exception e) {
        RestResponse<Object> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setError(e.getMessage());
        response.setMessage("404 Not Found. URL may be not exist..");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { StorageException.class })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exeption upload file...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { PermissionException.class })
    public ResponseEntity<RestResponse<Object>> handlePermissionException(Exception exception) {
        RestResponse<Object> response = new RestResponse<Object>();
        response.setStatusCode(HttpStatus.FORBIDDEN.value());
        response.setMessage("Forbidden");
        response.setError(exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}
