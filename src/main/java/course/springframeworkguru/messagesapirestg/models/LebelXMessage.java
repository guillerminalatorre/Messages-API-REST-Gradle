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
@Table(name = "labels_x_messages")
public class LebelXMessage implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_message",
            foreignKey = @ForeignKey(name="FK_MESSAGE_LABEL"))
    @PrimaryKeyJoinColumn(name = "id_message",referencedColumnName="id_message")
    private Message message;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_label",
            foreignKey = @ForeignKey(name="FK_LABEL_MESSAGE"))
    @PrimaryKeyJoinColumn(name = "id_label",referencedColumnName="id_label")
    private Label label;
}
