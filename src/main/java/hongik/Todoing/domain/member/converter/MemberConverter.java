package hongik.Todoing.domain.member.converter;

import hongik.Todoing.domain.member.dto.response.GetProfileDTO;

public class MemberConverter {

    public static GetProfileDTO toGetProfileDTO(String email, String name) {
        return new GetProfileDTO(email, name);
    }
}
