package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelXMessageRepository extends JpaRepository<LabelXMessage, Integer> {

    List<LabelXMessage> findByMessageIdAndUserIdAndLabelIsEnabledTrue(int idMessage, int idUser);
}
