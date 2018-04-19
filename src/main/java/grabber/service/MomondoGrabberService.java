package grabber.service;

import grabber.repository.*;
import grabber.repository.model.*;
import grabber.service.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MomondoGrabberService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TravellersRepository travellersRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private KidRepository kidRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DestinationSetupRepository destinationSetupRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DestinationOptionRepository destinationOptionRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TravelInfoRepository travelInfoRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PriceInfoRepository priceInfoRepository;

    private static Set<DestinationOption> getDestinationOptions(DestinationSetup destinationSetup) {
        Set<DestinationOption> destinationOptions = new HashSet<>();
        LocalDate fromDate = destinationSetup.getFromDate();
        LocalDate toDate = fromDate.plusDays(destinationSetup.getMinDuration());
        LocalDate today = LocalDate.now();
        while (!fromDate.isAfter(destinationSetup.getToDate().minusDays(destinationSetup.getMinDuration()))) {
            while (fromDate.isAfter(today) && !toDate.isAfter(destinationSetup.getToDate())) {
                DestinationOption destinationOption = new DestinationOption();
                destinationOption.setFromCity(destinationSetup.getFromCity());
                destinationOption.setToCity(destinationSetup.getToCity());
                destinationOption.setFromDate(fromDate);
                destinationOption.setToDate(toDate);
                destinationOptions.add(destinationOption);

                toDate = toDate.plusDays(1);
            }
            fromDate = fromDate.plusDays(1);
            toDate = fromDate.plusDays(destinationSetup.getMinDuration());
        }

        return destinationOptions;
    }

    public Set<TravelInfoDTO> getTravelInfos() {
        List<TravelInfo> allTravelInfos = travelInfoRepository.findAll();
        if (allTravelInfos.isEmpty()) {
            Travellers travellers = createDefaultTravellers();
            createDefaultDestinationSetup(travellers);

            allTravelInfos = travelInfoRepository.findAll();
        }
        Set<TravelInfoDTO> travelInfos = new HashSet<>();
        for (TravelInfo travelInfo : allTravelInfos) {
            TravelInfoDTO travelInfoDTO = new TravelInfoDTO();
            travelInfoDTO.setId(travelInfo.getId());

            Travellers travellers = travelInfo.getTravellers();
            TravellersDTO travellersDTO = new TravellersDTO();
            travellersDTO.setId(travellers.getId());
            travellersDTO.setNrOfAdults(travellers.getNrOfAdults());
            travellersDTO.setKids(new HashSet<>());
            Set<Kid> allKids = kidRepository.findByTravellers(travellers);
            for (Kid kid : allKids) {
                KidDTO newKid = new KidDTO(kid);
                travellersDTO.getKids().add(newKid);
            }
            travelInfoDTO.setTravellers(travellersDTO);

            travelInfoDTO.setDestinationOption(new DestinationOptionDTO(travelInfo.getDestinationOption()));
            travelInfos.add(travelInfoDTO);
        }

        return travelInfos;
    }

    public void updateTravelInfos(Set<TravelInfoDTO> travelInfos) {
        for (TravelInfoDTO travelInfo : travelInfos) {
            Optional<TravelInfo> optionalTravelInfo = travelInfoRepository.findById(travelInfo.getId());
            TravelInfo savedTravelInfo = optionalTravelInfo.get();
            Hibernate.initialize(savedTravelInfo.getPriceInfoHistory());

            PriceInfoDTO latestPriceInfo = travelInfo.getLatestPriceInfo();
            if (latestPriceInfo.getBest() == 0) {
                continue;
            }

            PriceInfo priceInfo = new PriceInfo();
            priceInfo.setBest(latestPriceInfo.getBest());
            priceInfo.setFastest(latestPriceInfo.getFastest());
            priceInfo.setMinimal(latestPriceInfo.getMinimal());
            priceInfo.setCaptureTime(latestPriceInfo.getCaptureTime());
            priceInfo.setTravelInfo(savedTravelInfo);
            PriceInfo savedPriceInfo = priceInfoRepository.save(priceInfo);
            savedTravelInfo.setLatestPriceInfo(savedPriceInfo);
            savedTravelInfo.getPriceInfoHistory().add(savedPriceInfo);

            travelInfoRepository.save(savedTravelInfo);
        }
    }

    private void createDefaultDestinationSetup(Travellers travellers) {
        DestinationSetup destinationSetup = new DestinationSetup();
        destinationSetup.setFromCity("Tallinn");
        destinationSetup.setToCity("Malaga");
        destinationSetup.setFromDate(LocalDate.of(2018, 8, 14));
        destinationSetup.setToDate(LocalDate.of(2018, 8, 28));
        destinationSetup.setMinDuration(7);
        createDestinationSetup(destinationSetup, travellers);
    }

    private void createDestinationSetup(DestinationSetup destinationSetup, Travellers travellers) {
        destinationSetupRepository.save(destinationSetup);

        Set<DestinationOption> destinationOptions = getDestinationOptions(destinationSetup);
        for (DestinationOption destinationOption : destinationOptions) {
            DestinationOption savedOption = destinationOptionRepository.save(destinationOption);

            TravelInfo travelInfo = new TravelInfo();
            travelInfo.setDestinationOption(savedOption);
            travelInfo.setTravellers(travellers);

            travelInfoRepository.save(travelInfo);
        }
    }

    private Travellers createDefaultTravellers() {
        Travellers travellers = new Travellers();
        travellers.setNrOfAdults(2);
        Travellers savedTravellers = travellersRepository.save(travellers);

        Kid kid = new Kid();
        kid.setBirthday(LocalDate.of(2011, 1, 8));
        kid.setTravellers(savedTravellers);
        kidRepository.save(kid);

        return savedTravellers;
    }
}
