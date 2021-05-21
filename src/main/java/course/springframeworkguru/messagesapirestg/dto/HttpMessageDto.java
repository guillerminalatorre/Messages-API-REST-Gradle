package course.springframeworkguru.messagesapirestg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HttpMessageDto {
    private String message;
    private String details;
}
