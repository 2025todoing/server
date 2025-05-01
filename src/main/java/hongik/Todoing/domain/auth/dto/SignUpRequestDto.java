package hongik.Todoing.domain.auth.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDto {

    private String name;
    private String email;
    private String password;
}
