package wanted.assignment.pmsystem.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {
    private int status;
    private String errorCode;
    private String message;
}