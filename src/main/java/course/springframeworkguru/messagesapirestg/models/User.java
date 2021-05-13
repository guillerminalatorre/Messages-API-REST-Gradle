package course.springframeworkguru.messagesapirestg.models;

import course.springframeworkguru.messagesapirestg.models.employeesAPI.Employee;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User  implements Serializable {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "is_admin")
    public boolean isAdmin;

    @OneToOne
    @JoinColumn(name = "id_employee", foreignKey = @ForeignKey(name="FK_EMPLOYEE_USER"))
    public Employee employee;

}
