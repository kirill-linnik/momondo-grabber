package grabber.service.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TravellersWithKids {
    private Integer id;
    private int nrOfAdults;
    private Set<Kid> kids;

    @Data
    public static class Kid {
        private Integer id;
        private LocalDate birthday;
    }
}
