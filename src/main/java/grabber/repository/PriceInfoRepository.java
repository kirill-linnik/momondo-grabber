package grabber.repository;

import grabber.repository.model.PriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceInfoRepository extends JpaRepository<PriceInfo, Integer> {
}
