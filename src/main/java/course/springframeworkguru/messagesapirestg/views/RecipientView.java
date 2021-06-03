package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface RecipientView {
    @JsonUnwrapped
    UserView getUser();
    @JsonUnwrapped
    RecipientTypeView getRecipientType();

    void setUser(UserView user);
    void setRecipientType(RecipientTypeView recipientType);
}
