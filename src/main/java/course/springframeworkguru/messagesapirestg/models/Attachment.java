package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attatchments")
public class Attachment  implements Serializable {

    @Id
    @Column(name = "id_attachment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "attachment")
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "id_message", foreignKey = @ForeignKey(name="FK_MESSAGE_ATTACHMENT"))
    @JsonBackReference
    @JsonIgnore
    private Message message;
}
