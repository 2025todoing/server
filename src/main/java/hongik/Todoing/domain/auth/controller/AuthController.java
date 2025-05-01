package hongik.Todoing.domain.auth.controller;

import hongik.Todoing.domain.auth.converter.AuthConverter;
import hongik.Todoing.domain.auth.dto.LoginRequestDTO;
import hongik.Todoing.domain.auth.dto.SignUpRequestDto;
import hongik.Todoing.domain.auth.service.AuthService;
import hongik.Todoing.domain.jwt.JwtUtil;
import hongik.Todoing.domain.jwt.dto.JwtDTO;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.global.apiPayload.ApiResponse;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@RestController
@AllArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @GetMapping("/api/reissue")
    public ApiResponse<JwtDTO> reissueToken(@RequestHeader("RefreshToken") String refreshToken ) {
        try {
            jwtUtil.validateRefreshToken(refreshToken);
            return ApiResponse.onSuccess(
                    jwtUtil.reissueToken(refreshToken)
            );
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (ExpiredJwtException e) {
            throw  new GeneralException(ErrorStatus.TOKEN_EXPIRED);
        }
    }

    @GetMapping("/auth/login/kakao")
    public ApiResponse<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse response) {
        Member member = authService.oAuthLogin(accessCode, response);

        String accessToken = response.getHeader("Authorization");
        return ApiResponse.onSuccess(AuthConverter.JoinResponse(member, accessToken));
    }

    @PostMapping("/auth/login/email")
    public ApiResponse<JwtDTO> loginWithEmail(@RequestBody LoginRequestDTO request) {
        JwtDTO jwtDTO = authService.loginWithEmail(request.email(), request.password());
        return ApiResponse.onSuccess(jwtDTO);
    }

    @PostMapping("/auth/signup")
    public ApiResponse<JwtDTO> signUpWithEmail(@RequestBody SignUpRequestDto request) {
        JwtDTO jwtDTO = authService.signUpWithEmail(request);
        return ApiResponse.onSuccess(jwtDTO);
    }


}
