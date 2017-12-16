package ie.britoj.currencyexchangerates.services;


import ie.britoj.currencyexchangerates.openexchangerates.ExchangeRatesQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class ExchangeRatesService implements CurrencyExchangeProvider{


    @Autowired
    RestOperations restOperations;

    @Autowired
    Environment environment;
    public ExchangeRatesQueryResult retrieveActualCurrencyExchangeRate(String currencyBase){
        String latestExchangesURI = getExchangeRatesURI("latest") + "?base="+currencyBase;
        return restOperations.getForObject(latestExchangesURI, ExchangeRatesQueryResult.class);
    }

    @Override
    public Map<String, String> retrieveAllCurrencies() {
        String currencyResource = environment.getRequiredProperty("currency-list-api.endpoint");
        return restOperations.getForObject(currencyResource, Map.class);

    }

    @Override
    public ExchangeRatesQueryResult retrieveHistoricalCurrencyExchangeRate(String currencyBase, LocalDate localDate) {
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String latestExchangesURI = getExchangeRatesURI(formattedDate) + "?base="+currencyBase;
        return restOperations.getForObject(latestExchangesURI, ExchangeRatesQueryResult.class);

    }

    private String getExchangeRatesURI(String resource){
        String apiEndpoint = this.environment.getRequiredProperty("currency-exchange-api.endpoint");
        return apiEndpoint + resource ;
    }
}
