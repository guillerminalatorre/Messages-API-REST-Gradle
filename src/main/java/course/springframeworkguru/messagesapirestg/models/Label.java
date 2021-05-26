package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "enabled", columnDefinition = "boolean default true")
    @JsonIgnore
    private boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_LABEL"))
    @JsonIgnore
    private User user;

}
