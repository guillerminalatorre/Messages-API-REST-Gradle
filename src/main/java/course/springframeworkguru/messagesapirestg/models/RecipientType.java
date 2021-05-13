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
@Table(name = "recipient_types")
public class RecipientType implements Serializable {

    @Id
    @Column(name = "id_recipient_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "acronym")
    private String acronym;
}
