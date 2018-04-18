package grabber.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate birthday;

    @ManyToOne
    @JoinColumn(name = "travellers_id", nullable = false)
    private Travellers travellers;

}
