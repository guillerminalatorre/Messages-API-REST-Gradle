package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.views.LabelView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label,Integer> {

    List<LabelView> findByIsEnabledTrueAndUserIdOrUserId(int id, Integer id2);

    Label findByIdAndIsEnabledTrue(int id);
}