package course.springframeworkguru.messagesapirestg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    private String mailUsername;
    private String password;
}
