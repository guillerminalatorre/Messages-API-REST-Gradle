package course.springframeworkguru.messagesapirestg.models.employeesAPI;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "states")
public class State  implements Serializable {
    @Id
    @Column(name = "id_state")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_country", foreignKey = @ForeignKey(name="FK_COUNTRY_STATE"))
    private Country country;
}
