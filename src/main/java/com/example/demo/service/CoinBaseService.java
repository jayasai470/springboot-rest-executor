package com.example.demo.service;

import com.example.demo.pojo.Currency;
import com.example.demo.pojo.ExchangeRate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoinBaseService {


    private final RestTemplate coinBaseCurrenciesRestTemplate;
    private final RestTemplate coinBaseExchangeRateRestTemplate;
    private final RestTemplate coinBasePricesRestTemplate;
    private final ExecutorService executorService;

    public CoinBaseService(@Qualifier("coinBaseCurrencies") RestTemplate coinBaseCurrenciesRestTemplate, @Qualifier("coinBaseExchangeRate") RestTemplate coinBaseExchangeRateRestTemplate, @Qualifier("coinBasePrices")
            RestTemplate coinBasePricesRestTemplate, ExecutorService threadPoolExecutor) {
        this.coinBaseCurrenciesRestTemplate = coinBaseCurrenciesRestTemplate;
        this.coinBaseExchangeRateRestTemplate = coinBaseExchangeRateRestTemplate;
        this.coinBasePricesRestTemplate = coinBasePricesRestTemplate;
        this.executorService = threadPoolExecutor;
    }

    public List<Currency> getCoinBaseCurrencies() {
        return coinBaseCurrenciesRestTemplate
                .exchange("/v2/currencies", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<Currency>>>() {
                }).getBody().get("data");

//        return coinBaseCurrenciesRestTemplate.exchange("/v2/currencies", HttpMethod.GET, null, new ParameterizedTypeReference<ResponseWrapper<Currency>>() {
//        }).getBody().getData();

    }

    public ExchangeRate getExchangeRate(String curr) {
        return this.coinBaseExchangeRateRestTemplate.exchange("/v2/exchange-rates?currency={CURR_CODE}", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, ExchangeRate>>(){}, curr).getBody().get("data");
    }


    //synchronous
    public List<Currency> makeSyncCalls() {
        return getCoinBaseCurrencies().stream().map(currency -> {
            log.info("==========> making exchange rate call for {}", currency.getName());
            currency.getAdditionalProperties().put(currency.getId(), getExchangeRate(currency.getId()));
            log.info("done making exchange rate call for {} <===========", currency.getName());
            return currency;
        }).collect(Collectors.toList());
    }

    public List<Currency> maskeAsyncParallelCalls() {
        return getCoinBaseCurrencies().parallelStream().map(currency -> {
            log.info("==========> making exchange rate call for {}", currency.getName());
            currency.getAdditionalProperties().put(currency.getId(), getExchangeRate(currency.getId()));
            log.info("done making exchange rate call for {} <===========", currency.getName());
            return currency;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Currency> maskeAsyncParallelCallsWithThreadPool() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        return forkJoinPool.submit(() -> {
            return getCoinBaseCurrencies().parallelStream().map(currency -> {
                log.info("==========> making exchange rate call for {}", currency.getName());
                currency.getAdditionalProperties().put(currency.getId(), getExchangeRate(currency.getId()));
                log.info("done making exchange rate call for {} <===========", currency.getName());
                return currency;
            }).collect(Collectors.toList());
        }).get();
    }

    @SneakyThrows
    public List<Currency> makeAsyncCallWithThreadPoolExecutor() {

        List<Currency> coinBaseCurrencies = getCoinBaseCurrencies();
        List<Callable<ExchangeRate>> callables = new ArrayList<>();

        for(Currency currency : coinBaseCurrencies) {
            callables.add(new Callable<ExchangeRate>() {

                @Override
                public ExchangeRate call() throws Exception {
                    log.info("callable =======> {}", currency.getName());
                    return getExchangeRate(currency.getId());
                }
            });
        }

        List<Future<ExchangeRate>> futures = executorService.invokeAll(callables);

        futures.stream().forEach(exchangeRateFuture -> {
            try {
                ExchangeRate exchangeRate = exchangeRateFuture.get();
                log.info("got the value {} ", exchangeRate.getCurrency());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        return coinBaseCurrencies;


    }

}