package grabber.repository;

import grabber.repository.model.Kid;
import grabber.repository.model.Travellers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface KidRepository extends JpaRepository<Kid, Integer> {

    Set<Kid> findByTravellers(Travellers travellers);

}
