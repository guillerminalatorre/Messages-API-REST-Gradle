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
@Table(name = "messages")
public class Message  implements Serializable {

    @Id
    @Column(name = "id_message")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "subject")
    public String subject;

    @Column(name = "body")
    public String body;

    @ManyToOne
    @JoinColumn(name = "id_user_from", foreignKey = @ForeignKey(name="FK_USER_FROM"))
    public User userFrom;
}
