package course.springframeworkguru.messagesapirestg.models.employees;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cities")
public class City  implements Serializable {

    @Id
    @Column(name = "zip_code")
    private Long zipCode;

    @Column( name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_state", foreignKey = @ForeignKey(name="FK_STATE_CITY"))
    private State state;
}
