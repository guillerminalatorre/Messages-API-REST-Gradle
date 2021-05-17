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
@Table(name = "labels")
public class Label  implements Serializable {

    @Id
    @Column(name= "id_label")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name= "name")
    private String name;

}
