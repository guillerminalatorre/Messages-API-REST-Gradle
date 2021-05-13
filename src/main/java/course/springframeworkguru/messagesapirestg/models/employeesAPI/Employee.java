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
@Table(name = "employees")
public class Employee  implements Serializable {
    @Id
    @Column(name = "id_employee")
    private Long id;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mail_username")
    private String mailUsername;

    @ManyToOne
    @JoinColumn(name = "id_city", foreignKey = @ForeignKey(name="FK_CITY_EMPLOYEE"))
    private City city;
}
