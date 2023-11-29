package wanted.assignment.pmsystem.domain.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.assignment.pmsystem.domain.user.cache.UserInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;

    @PostMapping(value = "/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String userInfo = userInfoService.cacheUserInfo(request);
        return ResponseEntity.ok().body(userInfo);
    }

    @PostMapping(value = "/me/reload")
    public ResponseEntity<?> reloadUserInfo(HttpServletRequest request) {
        String userInfo = userInfoService.reloadUserInfo(request);
        return ResponseEntity.ok().body(userInfo);
    }
}
