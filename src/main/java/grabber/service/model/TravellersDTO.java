package grabber.service.model;

import lombok.Data;

import java.util.Set;

@Data
public class TravellersDTO {
    private Integer id;
    private int nrOfAdults;
    private Set<KidDTO> kids;
}
