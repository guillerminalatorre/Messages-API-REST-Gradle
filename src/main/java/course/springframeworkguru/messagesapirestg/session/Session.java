package course.springframeworkguru.messagesapirestg.session;

import course.springframeworkguru.messagesapirestg.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Session {

    String token;
    User loggedUser;
    Date lastAction;
}
