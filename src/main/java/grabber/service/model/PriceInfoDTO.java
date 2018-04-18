package grabber.service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PriceInfoDTO {

    private int minimal;
    private int fastest;
    private int best;
    private LocalDateTime captureTime;
}
