package course.springframeworkguru.messagesapirestg.exceptions;


import lombok.Data;

@Data
public class LabelException extends Throwable{

    private String details;

    public LabelException(String message, String details) {
        super(message);
        this.details = details;
    }

}
