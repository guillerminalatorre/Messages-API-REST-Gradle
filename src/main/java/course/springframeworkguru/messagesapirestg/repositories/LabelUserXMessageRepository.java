package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.LabelUserXMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelUserXMessageRepository extends JpaRepository<LabelUserXMessage, Integer> {

    List<LabelUserXMessage> findByMessageIdAndLabelUserUserId(int idMessage, int idUser);

    List<LabelUserXMessage> findByLabelUserUserId(int idUser);
}
