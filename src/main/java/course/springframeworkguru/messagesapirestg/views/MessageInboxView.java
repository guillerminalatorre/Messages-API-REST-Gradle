package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

@JsonPropertyOrder({"id","date", "userFrom", "recipients","subject", "body", "attachments"})
public interface MessageInboxView {

    int getId();

    @JsonProperty("date")
    String getDatee();

    UserView getUserFrom();

    @JsonProperty("recipients")
    List<RecipientView> getRecipientListInbox();

    String getSubject();

    String getBody();

    @JsonProperty("attachments")
    List<AttachmentView> getAttachmentsList();
}
