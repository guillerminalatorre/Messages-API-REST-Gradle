package course.springframeworkguru.messagesapirestg.dto.input;

import course.springframeworkguru.messagesapirestg.dto.output.RecipientDto;
import lombok.Data;

import java.util.List;

@Data
public class MessageDto {

    private String subject;
    private String body;
    private List<RecipientDto> recipients;
    private String[] attachments;
}
