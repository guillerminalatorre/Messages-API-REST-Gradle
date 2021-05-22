package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.Data;

@Data
public class LoginException extends Throwable {

    private String details;


    public LoginException(String message, String details) {
        super(message);
        this.details = details;
    }
}
