package vn.trinhlam.jobhunter.domain.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.trinhlam.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateDTO {
    private long id;
    private String name;
    private GenderEnum genderEnum;
    private String address;
    private int age;
    private Instant updateInstant;
}
