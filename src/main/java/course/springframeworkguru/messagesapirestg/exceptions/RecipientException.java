package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipientException extends Throwable{

    private String details;

    public RecipientException(String message, String details) {
        super(message);
        this.details =details;
    }
}
