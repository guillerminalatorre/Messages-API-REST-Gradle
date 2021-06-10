package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserException extends Throwable {

    private String details;

    public UserException(String message, String details) {
        super(message);
        this.details =details;
    }
}
