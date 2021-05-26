package course.springframeworkguru.messagesapirestg.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewMessageDto {

    private String subject;
    private String body;
    private List<RecipientDto> recipients;
    private String[] attachments;
}
