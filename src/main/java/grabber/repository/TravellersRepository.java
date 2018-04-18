package grabber.repository;

import grabber.repository.model.Travellers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravellersRepository extends JpaRepository<Travellers, Integer> {
}
