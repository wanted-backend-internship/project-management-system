package wanted.assignment.pmsystem.domain.user.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import wanted.assignment.pmsystem.domain.user.User;
import wanted.assignment.pmsystem.global.util.AuthUtil;
import wanted.assignment.pmsystem.global.util.RedisUtil;
import wanted.assignment.pmsystem.global.util.TokenUtil;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final AuthUtil authUtil;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    @Cacheable(value = "userInfo", key="@authUtil.getLoginUserIndex().toString()")
    public UserDTO cacheUserInfo() {
        User user = authUtil.getLoginUser();
        return toDTO(user);
    }

    public String cacheUserInfo(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(tokenUtil.getJWTTokenFromHeader(request));
        String userInfo = redisUtil.getData("userInfo::" + userId);

        if (userInfo == null) {
            UserDTO userDTO = cacheUserInfo();
            try {
                String userJson = objectMapper.writeValueAsString(userDTO);
                redisUtil.setData("userInfo::" + userId, userJson);
                userInfo = userJson;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userInfo;
    }

    @CachePut(value = "userInfo", key="@authUtil.getLoginUserIndex().toString()")
    public UserDTO reloadUserInfo() {
        User user = authUtil.getLoginUser();
        return toDTO(user);
    }

    public String reloadUserInfo(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(tokenUtil.getJWTTokenFromHeader(request));
        UserDTO userDTO = reloadUserInfo();

        try {
            String userJson = objectMapper.writeValueAsString(userDTO);
            redisUtil.setData("userInfo::" + userId, userJson);
            return userJson;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(String.valueOf(user.getId()));
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
