package grabber;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import grabber.service.model.DestinationOptionDTO;
import grabber.service.model.KidDTO;
import grabber.service.model.PriceInfoDTO;
import grabber.service.model.TravellersDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MomondoPageHandler {

    private static final Logger log = LogManager.getLogger(MomondoPageHandler.class);

    private DestinationOptionDTO destinationOption;
    private TravellersDTO travellers;

    public MomondoPageHandler(DestinationOptionDTO destinationOption, TravellersDTO travellers) {
        this.destinationOption = destinationOption;
        this.travellers = travellers;
    }

    private static int getPrice(String str) {
        str = str.replaceAll("[^0-9]", "");
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    PriceInfoDTO getPriceInfo() {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        try (BrowserWebDriverContainer browser =
                     new BrowserWebDriverContainer("selenium/standalone-firefox:3.11.0")
                             .withDesiredCapabilities(capabilities)) {
            browser.start();

            RemoteWebDriver driver = browser.getWebDriver();
            WebDriverRunner.setWebDriver(driver);
            String url = getRequestURL();

            log.info("About to open page: " + url);
            Selenide.open(url);
            $(By.className("c-flights-result-aside-results-search-label")).waitUntil(visible, 30000);
            $(By.className("c-flights-result-aside-results-search-label")).waitUntil(text("Otsing lõppenud"), 30000);
            log.info("Flight results are loaded");
            ElementsCollection prices = $$(By.tagName("toggle-group-item"));
            PriceInfoDTO priceInfo = new PriceInfoDTO();
            priceInfo.setCaptureTime(LocalDateTime.now());
            for (SelenideElement priceElement : prices) {
                SelenideElement priceTitle = priceElement.$(By.className("c-flights_result_sortbar-item-headline"));
                SelenideElement priceValue = priceElement.$(By.className("c-flights_result_sortbar-item-description"));
                if (!priceTitle.exists() || !priceValue.exists()) {
                    continue;
                }
                int price = getPrice(priceValue.getText());
                switch (priceTitle.getText()) {
                    case "Kiireim":
                        priceInfo.setFastest(price);
                        break;
                    case "Odavaim":
                        priceInfo.setMinimal(price);
                        break;
                    case "Parim":
                        priceInfo.setBest(price);
                        break;
                    default:
                        break;
                }
            }

            close();
            log.info(priceInfo);

            return priceInfo;
        }
    }

    private String getRequestURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.momondo.ee/lennupiletid/search/");
        sb.append(destinationOption.getFromCity().toLowerCase().replace(" ", "-"));
        sb.append("/");
        sb.append(destinationOption.getToCity().toLowerCase().replace(" ", "-"));
        sb.append("/");
        sb.append(destinationOption.getFromDate());
        sb.append("/");
        sb.append(destinationOption.getToDate());
        sb.append("?tc=eco&na=false&ac=");
        sb.append(travellers.getNrOfAdults());
        sb.append("&dp=false&currency=EUR&route_referrer=searchform");
        Set<KidDTO> kids = travellers.getKids();
        if (!kids.isEmpty()) {
            sb.append("&ca=");
            List<String> ages = kids.stream().map(kid -> Integer.toString(getKidAge(kid.getBirthday()))).collect(Collectors.toList());
            sb.append(String.join(",", ages));
        }
        sb.append("&a=");
        sb.append(Math.random());
        return sb.toString();
    }

    private int getKidAge(LocalDate birthDate) {
        return Period.between(birthDate, destinationOption.getToDate()).getYears();
    }
}
