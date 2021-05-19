package course.springframeworkguru.messagesapirestg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;

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
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_employee", foreignKey = @ForeignKey(name="FK_EMPLOYEE_USER"))
    private Employee employee;

}
