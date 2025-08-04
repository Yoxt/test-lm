package adeo.leroymerlin.cdp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
    void deleteById(Long eventId);
    Optional<Event> findById(Long id);
}
