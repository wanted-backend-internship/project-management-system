package wanted.assignment.pmsystem.global.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import wanted.assignment.pmsystem.domain.user.User;
import wanted.assignment.pmsystem.domain.user.UserRepository;
import wanted.assignment.pmsystem.global.exception.ApiException;
import wanted.assignment.pmsystem.global.exception.ErrorType;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public Long getLoginUserIndex() {
        User user = getLoginUser();
        Long userId = user.getId();
        return userId;
    }

    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (User) principal;
    }

    public User getLoginUserForLazy() {
        Long id = getLoginUserIndex();
        User user = userRepository.findById(id).get();
        return user;
    }
}
