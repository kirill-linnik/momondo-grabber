package grabber.service.model;

import grabber.repository.model.DestinationOption;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DestinationOptionDTO {
    private Integer id;
    private String fromCity;
    private String toCity;
    private LocalDate fromDate;
    private LocalDate toDate;

    public DestinationOptionDTO(DestinationOption destinationOption) {
        id = destinationOption.getId();
        fromCity = destinationOption.getFromCity();
        toCity = destinationOption.getToCity();
        fromDate = destinationOption.getFromDate();
        toDate = destinationOption.getToDate();
    }
}
