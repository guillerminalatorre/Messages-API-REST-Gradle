package course.springframeworkguru.messagesapirestg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RecipientDto {
    private String mailUsername;
    private String acronymRecipientType;
}
