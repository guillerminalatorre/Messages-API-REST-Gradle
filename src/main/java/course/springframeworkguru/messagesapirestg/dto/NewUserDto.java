package course.springframeworkguru.messagesapirestg.dto;

import lombok.*;

@Data
public class NewUserDto {

    private String username;
    private String password;
    private String mailUsername;

    @Override
    public String toString() {
        return "NewUserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mailUsername='" + mailUsername + '\'' +
                '}';
    }
}
