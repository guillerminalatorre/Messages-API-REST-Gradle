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
@Table(name = "labels_x_users")
public class LabelXUser implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_user",
            foreignKey = @ForeignKey(name="FK_USER_LABEL"))
    @PrimaryKeyJoinColumn(name = "id_user",referencedColumnName="id_user")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_label",
            foreignKey = @ForeignKey(name="FK_LABEL_USER"))
    @PrimaryKeyJoinColumn(name = "id_label",referencedColumnName="id_label")
    private Label label;
}
