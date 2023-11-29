package wanted.assignment.pmsystem.domain.user.cache;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO implements Serializable {
    private String userId;
    private String email;
    private String username;
}
