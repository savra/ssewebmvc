package com.hvdbs.ssewebmvc;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class StockPriceChanger {
    private final ApplicationEventPublisher publisher; //1

    private final Random random = new Random(); //2

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); //3

    public StockPriceChanger(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.executor.schedule(this::changePrice, 1, SECONDS); //4
    }

    private void changePrice() {
        BigDecimal price = BigDecimal.valueOf(random.nextGaussian()); //5
        publisher.publishEvent(new Stock(price)); //6
        executor.schedule(this::changePrice, random.nextInt(10000), MILLISECONDS); //7
    }
}
