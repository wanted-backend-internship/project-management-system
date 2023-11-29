package wanted.assignment.pmsystem.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.auth.dto.LoginRequest;
import wanted.assignment.pmsystem.domain.auth.dto.SignupRequest;
import wanted.assignment.pmsystem.global.exception.ApiException;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            authService.signup(signupRequest);
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus())
                    .body(apiException.getErrorType().getMessage());
        }
        return ResponseEntity.ok().body("회원 가입 성공");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        try {
            authService.login(loginRequest, httpServletResponse);
        } catch (ApiException apiException) {
            return ResponseEntity.status(apiException.getErrorType().getStatus())
                    .body(apiException.getErrorType().getMessage());
        }
        return ResponseEntity.ok().body("로그인 성공");
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpServletResponse) {
        authService.logout(httpServletResponse);
        return ResponseEntity.ok().body("로그 아웃 성공");
    }
}
