package grabber.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class TravelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private DestinationOption destinationOption;

    @OneToOne
    private PriceInfo latestPriceInfo;

    @OneToMany
    private Set<PriceInfo> priceInfoHistory;
}
