package grabber.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class PriceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int minimal;
    private int fastest;
    private int best;
    private LocalDateTime captureTime;

    @ManyToOne
    @JoinColumn(name = "travel_info_id", nullable = false)
    private TravelInfo travelInfo;

    @Override
    public String toString() {
        return "minimal: " + minimal + "; fastest: " + fastest + "; best: " + best;
    }
}
