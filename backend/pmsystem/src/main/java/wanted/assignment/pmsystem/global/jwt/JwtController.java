package wanted.assignment.pmsystem.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;
import wanted.assignment.pmsystem.global.util.CookieUtil;
import wanted.assignment.pmsystem.global.util.RedisUtil;
import wanted.assignment.pmsystem.global.util.TokenUtil;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class JwtController {
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, "refreshToken");
        if (refreshTokenCookie == null) { // 쿠키가 존재하지 않는 경우
            throw new ApiException(ErrorType.REFRESH_TOKEN_DOES_NOT_EXIST);
        }

        String refreshToken = refreshTokenCookie.getValue();
        String userId = tokenUtil.getUserIdFromToken(refreshToken);
        log.info("====================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("userId: " + userId);
        log.info("====================================================");

        if (redisUtil.getData(userId) == null) {
            String newRefreshToken = tokenUtil.generateRefreshToken(userId);
            redisUtil.setDataExpire("refreshToken", newRefreshToken, Duration.ofDays(7));
            cookieUtil.create(refreshToken, response);
        }

        // 어세스 토큰 재발급
        String newAccessToken;
        newAccessToken = "Bearer " + tokenUtil.refreshAccessToken(refreshToken);

        // 헤더에 추가
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", newAccessToken);

        tokenUtil.getAuthenticationFromToken(newAccessToken);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}