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
@Table(name = "labels_users")
public class LabelUser implements Serializable {

    @Id
    @Column(name = "id_label_x_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_label",
            foreignKey = @ForeignKey(name="FK_LABEL_USER"))
    private Label label;

    @ManyToOne
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_LABEL"))
    @JsonIgnore
    private User user;
}
