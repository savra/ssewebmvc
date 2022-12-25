package com.hvdbs.ssewebmvc;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class StockController {
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>(); //2.1

    @GetMapping("/stocks-stream") //2.2
    public SseEmitter stocksStream() {
        SseEmitter sseEmitter = new SseEmitter();
        clients.add(sseEmitter);

        sseEmitter.onTimeout(() -> clients.remove(sseEmitter));
        sseEmitter.onError(throwable -> clients.remove(sseEmitter));

        return sseEmitter;
    }

    @Async
    @EventListener
    public void stockMessageHandler(Stock stock) { //2.3
        List<SseEmitter> errorEmitters = new ArrayList<>();

        clients.forEach(emitter -> {
            try {
                emitter.send(stock, MediaType.APPLICATION_JSON); //2.4
            } catch (Exception e) {
                errorEmitters.add(emitter);
            }
        });

        errorEmitters.forEach(clients::remove); //2.5
    }
}
