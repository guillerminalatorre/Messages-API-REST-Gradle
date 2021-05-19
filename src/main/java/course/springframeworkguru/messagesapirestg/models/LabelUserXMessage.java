package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "labels_users_x_messages")
public class LabelUserXMessage implements Serializable {

    @Id
    @Column(name = "id_label_x_user_x_message")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_message",
            foreignKey = @ForeignKey(name="FK_MESSAGE_LABEL_X_USER"))
    @JsonManagedReference
    @JsonIgnoreProperties({"subject", "body", "userFrom", "recipientList", "labelXMessageList"})
    private Message message;

    @ManyToOne
    @JoinColumn(name = "id_label_x_user",
            foreignKey = @ForeignKey(name="FK_LABEL_X_USER"))
    @JsonManagedReference
    private LabelUser labelUser;
}
