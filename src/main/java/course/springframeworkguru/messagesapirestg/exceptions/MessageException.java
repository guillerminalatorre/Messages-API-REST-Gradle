package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageException extends Throwable {

    private String details;

    public MessageException(String message, String details) {
        super(message);
        this.details =details;
    }
}
