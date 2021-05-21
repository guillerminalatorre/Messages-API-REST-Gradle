package course.springframeworkguru.messagesapirestg.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RecipientException extends Throwable{

    private String details;

    public RecipientException(String message, String details) {
        super(message);
        this.details =details;
    }
}
