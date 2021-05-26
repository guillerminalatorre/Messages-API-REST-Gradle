package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.views.MessageInboxView;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.views.MessageSentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {

    Page<MessageSentView> findDistinctByUserFromIdAndIsDeletedByUserFromFalse(int id, Pageable pageable);

    Page<MessageInboxView> findDistinctByRecipientListUserIdAndRecipientListIsDeletedByRecipientFalse(int id, Pageable pageable);

    Page<MessageInboxView> findDistinctByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndRecipientListIsDeletedByRecipientFalse (int idUser, int idUser2, int idLabel, Pageable pageable);

    Page<MessageSentView> findDistinctByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndIsDeletedByUserFromFalse(int idUser, int idUser2, int idLabel, Pageable pageable);

    Message findById(int id);
}
