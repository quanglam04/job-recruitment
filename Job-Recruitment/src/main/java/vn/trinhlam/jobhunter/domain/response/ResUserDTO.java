package vn.trinhlam.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.trinhlam.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {

    private long id;
    private String name;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private String email;
    private CompanyUser company;
    private RoleUser role;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }

}
