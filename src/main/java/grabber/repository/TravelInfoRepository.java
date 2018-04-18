package grabber.repository;

import grabber.repository.model.DestinationOption;
import grabber.repository.model.TravelInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelInfoRepository extends JpaRepository<TravelInfo, Integer> {

    TravelInfo findByDestinationOption(DestinationOption destinationOption);

}
