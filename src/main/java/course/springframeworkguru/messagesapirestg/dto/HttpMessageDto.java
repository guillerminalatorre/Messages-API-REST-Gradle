package course.springframeworkguru.messagesapirestg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class HttpMessageDto {
    private String message;
    private String details;
}
