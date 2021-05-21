package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.Data;

@Data
public class UserException extends Throwable {

    private String details;

    public UserException(String message, String details) {
        super(message);
        this.details =details;
    }
}
