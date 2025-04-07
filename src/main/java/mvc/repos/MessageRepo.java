package mvc.repos;

import mvc.domain.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findByTag(String tag, Sort sort);
    Optional<Message> findByUniqueId(String uniqueId);
    boolean existsByUniqueId(String uniqueId);

    void deleteByUniqueId(String uniqueId);
}
