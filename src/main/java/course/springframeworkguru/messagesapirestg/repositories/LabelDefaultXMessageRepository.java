package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.LabelDefaultXMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface LabelDefaultXMessageRepository extends JpaRepository<LabelDefaultXMessage, Integer> {

    List<LabelDefaultXMessage> findByMessageIdAndUserId (int idMessage, int idUser);
}
