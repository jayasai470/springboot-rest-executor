package com.example.demo.controller;

import com.example.demo.service.CoinBaseService;
import com.example.demo.pojo.Currency;
import com.example.demo.pojo.ExchangeRate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Api {

    private final CoinBaseService coinBaseService;

    public Api(CoinBaseService coinBaseService) {
        this.coinBaseService = coinBaseService;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<Currency> getBasic() {
        return coinBaseService.makeSyncCalls();
    }
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<Currency> getBasiccs() {
        return coinBaseService.getCoinBaseCurrencies();
    }

    @GetMapping(value = "/test2", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<Currency> getBasi2() {
        return coinBaseService.maskeAsyncParallelCalls();
    }

    @GetMapping(value = "/test3", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<Currency> getBasi3() {
        return coinBaseService.maskeAsyncParallelCallsWithThreadPool();
    }

    @GetMapping(value = "/test4", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<Currency> getBasi4() {
        return coinBaseService.makeAsyncCallWithThreadPoolExecutor();
    }

    @GetMapping(value = "/test/{curr}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ExchangeRate getBasic(@PathVariable String curr) {
        return coinBaseService.getExchangeRate(curr);
    }



}
