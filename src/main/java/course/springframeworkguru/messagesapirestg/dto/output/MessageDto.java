package course.springframeworkguru.messagesapirestg.dto.output;

import lombok.Data;

import java.util.List;

@Data
public class MessageDto {

    private String subject;
    private String body;
    private String mailUsernameFrom;
    private List<RecipientDto> recipients;
    private String[] attachments;
}
