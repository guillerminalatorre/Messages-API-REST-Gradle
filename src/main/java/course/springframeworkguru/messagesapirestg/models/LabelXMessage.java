package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "labels_x_messages")
public class LabelXMessage implements Serializable {

    @Id
    @Column(name = "id_label_x_message")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_message",
            foreignKey = @ForeignKey(name="FK_MESSAGE_LABEL"))
    @JsonManagedReference
    @JsonIgnoreProperties({"subject", "body", "userFrom", "recipientList", "labelXMessageList"})
    private Message message;


    @ManyToOne
    @JoinColumn(name = "id_label",
            foreignKey = @ForeignKey(name="FK_LABEL_MESSAGE"))
    private Label label;

    @ManyToOne
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_LABEL_MESSAGE"))
    @JsonIgnore
    private User user;
}