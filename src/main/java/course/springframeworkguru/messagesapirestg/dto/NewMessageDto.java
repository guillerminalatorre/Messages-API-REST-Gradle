package course.springframeworkguru.messagesapirestg.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewMessageDto {

    private String subject;
    private String body;
    private List<RecipientDto> recipients;
    private String[] attachments;
}
