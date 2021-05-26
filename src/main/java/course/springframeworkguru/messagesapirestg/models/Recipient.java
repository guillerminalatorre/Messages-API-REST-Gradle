package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipients")
public class Recipient implements Serializable {

    @Id
    @Column(name = "id_recipient")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_RECIPIENT"))
    @JsonIgnoreProperties(value = {"admin"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_message",
            foreignKey = @ForeignKey(name="FK_MESSAGE_RECIPIENT"))
    @JsonBackReference
    private Message message;

    @Column(name = "is_deleted_by_recipient", columnDefinition = "boolean default false")
    private boolean isDeletedByRecipient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_recipient_type",
            foreignKey = @ForeignKey(name="FK_TYPE_RECIPIENT"))
    private RecipientType recipientType;
}
