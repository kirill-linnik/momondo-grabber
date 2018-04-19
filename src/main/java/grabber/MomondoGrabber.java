package grabber;

import grabber.service.MomondoGrabberService;
import grabber.service.model.TravelInfoDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MomondoGrabber {

    private static final Logger log = LogManager.getLogger(MomondoGrabber.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MomondoGrabberService momondoGrabberService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void doMomondoCheck() {
        Set<TravelInfoDTO> travelInfos = momondoGrabberService.getTravelInfos();

        for (TravelInfoDTO travelInfo : travelInfos) {
            MomondoPageHandler handler = new MomondoPageHandler(travelInfo.getDestinationOption(), travelInfo.getTravellers());
            try {
                travelInfo.setLatestPriceInfo(handler.getPriceInfo());
            } catch (Exception e) {
                log.error("Cannot grab info for travel info id " + travelInfo.getId(), e);
            }
        }

        momondoGrabberService.updateTravelInfos(travelInfos);
    }


}
