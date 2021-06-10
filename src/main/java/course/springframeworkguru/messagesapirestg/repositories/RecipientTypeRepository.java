package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientTypeRepository extends JpaRepository<RecipientType, Integer> {
    RecipientType findByAcronym(String acronym);
}
