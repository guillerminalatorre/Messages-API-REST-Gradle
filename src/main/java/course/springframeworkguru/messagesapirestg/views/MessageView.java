package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","date", "userFrom", "recipients","subject", "body", "attachments"})
public interface MessageSentView {

    int getId();

    @JsonProperty("date")
    String getDatee();

    UserView getUserFrom();

    @JsonProperty("recipients")
    List<RecipientView> getRecipientList();

    String getSubject();

    String getBody();

    @JsonProperty("attachments")
    List<AttachmentView> getAttachmentsList();

    void setId(int id);
    void setDatee(String datee);
    void setUserFrom(UserView userFrom);
    void setRecipientList(List<RecipientView> recipientList);
    void setSubject(String subject);
    void setBody(String body);
    void setAttachmentsList(List<AttachmentView> attachmentsList);
}
