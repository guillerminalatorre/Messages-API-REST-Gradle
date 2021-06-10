package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.views.LabelXMessageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelXMessageRepository extends JpaRepository<LabelXMessage, Integer> {

    List<LabelXMessageView> findByMessageIdAndUserIdAndLabelIsEnabledTrue(int idMessage, int idUser);
}
