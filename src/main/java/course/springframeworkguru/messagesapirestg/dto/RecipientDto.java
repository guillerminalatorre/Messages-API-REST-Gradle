package course.springframeworkguru.messagesapirestg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipientDto {
    private String mailUsername;
    private String acronymRecipientType;
}
