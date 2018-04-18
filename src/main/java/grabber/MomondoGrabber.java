package grabber;

import grabber.repository.DestinationSetupRepository;
import grabber.repository.TravelInfoRepository;
import grabber.repository.model.DestinationOption;
import grabber.repository.model.DestinationSetup;
import grabber.repository.model.TravelInfo;
import grabber.service.TravellersService;
import grabber.service.model.TravellersWithKids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MomondoGrabber {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TravelInfoRepository travelInfoRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TravellersService travellersService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DestinationSetupRepository destinationSetupRepository;


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

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void doMomondoCheck() {
        List<DestinationSetup> allDestinationSetups = destinationSetupRepository.findAll();
        if (allDestinationSetups.isEmpty()) {
            createDefaultDestinationSetup();
            allDestinationSetups = destinationSetupRepository.findAll();
        }

        Set<TravellersWithKids> allTravellers = travellersService.getTravellersWithKids();
        if (allTravellers.isEmpty()) {
            travellersService.createDefaultTravellers();
            allTravellers = travellersService.getTravellersWithKids();
        }

        for (DestinationSetup destinationSetup : allDestinationSetups) {
            Set<DestinationOption> destinationOptions = getDestinationOptions(destinationSetup);
            for (DestinationOption destinationOption : destinationOptions) {
                for (TravellersWithKids travellers : allTravellers) {
                    MomondoPageHandler handler = new MomondoPageHandler(destinationOption, travellers);
                    TravelInfo travelInfo = new TravelInfo();
                    travelInfo.setDestinationOption(destinationOption);
                    travelInfo.setLatestPriceInfo(handler.getPriceInfo());

                    Set<TravelInfo> travelInfos = new HashSet<>();
                    travelInfos.add(travelInfo);
                }
            }
        }
    }

    private void createDefaultDestinationSetup() {
        DestinationSetup destinationSetup = new DestinationSetup();
        destinationSetup.setFromCity("Tallinn");
        destinationSetup.setToCity("Malaga");
        destinationSetup.setFromDate(LocalDate.of(2018, 8, 14));
        destinationSetup.setToDate(LocalDate.of(2018, 8, 28));
        destinationSetup.setMinDuration(7);
        destinationSetupRepository.save(destinationSetup);
    }
}
