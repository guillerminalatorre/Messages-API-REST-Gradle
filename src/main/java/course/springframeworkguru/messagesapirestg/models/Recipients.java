package course.springframeworkguru.messagesapirestg.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipients")
public class Recipients implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_RECIPIENT"))
    @PrimaryKeyJoinColumn(name = "id_user",referencedColumnName="id_user")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_message",
            foreignKey = @ForeignKey(name="FK_MESSAGE_RECIPIENT"))
    @PrimaryKeyJoinColumn(name = "id_message",referencedColumnName="id_message")
    private Message message;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recipient_type",
            foreignKey = @ForeignKey(name="FK_TYPE_RECIPIENT"))
    @PrimaryKeyJoinColumn(name = "id_recipient_type",referencedColumnName="id_recipient_type")
    private RecipientType recipientType;
}
