package grabber.service.model;

import grabber.repository.model.Kid;
import lombok.Data;

import java.time.LocalDate;

@Data
public class KidDTO {
    private Integer id;
    private LocalDate birthday;

    public KidDTO(Kid kid) {
        id = kid.getId();
        birthday = kid.getBirthday();
    }
}