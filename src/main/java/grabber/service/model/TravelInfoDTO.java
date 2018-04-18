package grabber.service.model;

import lombok.Data;

@Data
public class TravelInfoDTO {
    private Integer id;
    private TravellersDTO travellers;
    private DestinationOptionDTO destinationOption;
    private PriceInfoDTO latestPriceInfo;
}
