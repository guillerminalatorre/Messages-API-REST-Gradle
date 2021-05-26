package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private int id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_from", foreignKey = @ForeignKey(name="FK_USER_FROM"))
    private User userFrom;

    @JoinColumn(name="datee")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private final Date datee = this.currentDateTime() ;

    @Column(name = "is_deleted_by_user_from", columnDefinition = "boolean default false")
    private boolean isDeletedByUserFrom;

    @OneToMany(mappedBy = "message")
    @JsonManagedReference
    private List<Recipient> recipientList;

    @OneToMany(mappedBy = "message")
    @JsonManagedReference
    private List<Attachment> attachmentsList;

    @OneToMany(mappedBy = "message")
    @JsonIgnore
    private List<LabelXMessage> labelXMessageList;

    private Date currentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return new Date(dtf.format(now));
    }

    @JsonGetter("recipientList")
    public List<Recipient> getRecipientListInbox(){
        List<Recipient> recipients = recipientList
                .stream()
                .filter(r ->  !(r.getRecipientType().getAcronym().equals("BCC")))
                .collect(Collectors.toList());

        return recipients;
    }
}
