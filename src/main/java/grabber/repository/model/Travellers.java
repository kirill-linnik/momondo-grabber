package grabber.repository.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Travellers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int nrOfAdults;
}
