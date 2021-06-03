package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LoginException extends Throwable {

    private String details;


    public LoginException(String message, String details) {
        super(message);
        this.details = details;
    }
}
