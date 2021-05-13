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
@Table(name = "countries")
public class Country  implements Serializable {

    @Id
    @Column(name = "id_country")
    private Long id;

    @Column(name = "name")
    private String name;
}
