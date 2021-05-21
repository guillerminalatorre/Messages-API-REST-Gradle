package course.springframeworkguru.messagesapirestg.dto.output;

import lombok.Data;

@Data
public class LoginDto {

    private String mailUsername;
    private String password;
}
