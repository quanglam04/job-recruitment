package vn.trinhlam.jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    @NotBlank(message = "username không được để trống")
    private String username;
    @NotBlank(message = "password không được để trống")
    private String password;

}
