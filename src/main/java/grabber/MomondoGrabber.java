package grabber;

import grabber.service.MomondoGrabberService;
import grabber.service.model.TravelInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MomondoGrabber {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MomondoGrabberService momondoGrabberService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void doMomondoCheck() {
        Set<TravelInfoDTO> travelInfos = momondoGrabberService.getTravelInfos();

        for (TravelInfoDTO travelInfo : travelInfos) {
            MomondoPageHandler handler = new MomondoPageHandler(travelInfo.getDestinationOption(), travelInfo.getTravellers());
            travelInfo.setLatestPriceInfo(handler.getPriceInfo());
        }

        momondoGrabberService.updateTravelInfos(travelInfos);
    }


}
