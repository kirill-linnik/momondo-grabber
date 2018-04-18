package grabber.service;

import grabber.repository.KidRepository;
import grabber.repository.TravellersRepository;
import grabber.repository.model.Kid;
import grabber.repository.model.Travellers;
import grabber.service.model.TravellersWithKids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TravellersService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TravellersRepository travellersRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private KidRepository kidRepository;

    public Set<TravellersWithKids> getTravellersWithKids() {
        Set<TravellersWithKids> allTravellersWithKids = new HashSet<>();
        List<Travellers> allTravellers = travellersRepository.findAll();
        for (Travellers travellers : allTravellers) {
            TravellersWithKids travellersWithKids = new TravellersWithKids();
            travellersWithKids.setId(travellers.getId());
            travellersWithKids.setNrOfAdults(travellers.getNrOfAdults());
            travellersWithKids.setKids(new HashSet<>());
            Set<Kid> allKids = kidRepository.findByTravellers(travellers);
            for (Kid kid : allKids) {
                TravellersWithKids.Kid newKid = new TravellersWithKids.Kid();
                newKid.setId(kid.getId());
                newKid.setBirthday(kid.getBirthday());
                travellersWithKids.getKids().add(newKid);
            }
            allTravellersWithKids.add(travellersWithKids);
        }

        return allTravellersWithKids;
    }

    public void createDefaultTravellers() {
        Travellers travellers = new Travellers();
        travellers.setNrOfAdults(2);
        Travellers savedTravellers = travellersRepository.save(travellers);

        Kid kid = new Kid();
        kid.setBirthday(LocalDate.of(2011, 1, 8));
        kid.setTravellers(savedTravellers);
        kidRepository.save(kid);
    }
}
