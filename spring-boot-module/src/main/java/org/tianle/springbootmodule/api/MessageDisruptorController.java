package org.tianle.springbootmodule.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tianle.springbootmodule.disruptor.message.service.MessageDisruptorService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/bench/message")
public class MessageDisruptorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDisruptorController.class);

    private final MessageDisruptorService messageDisruptorService;

    public MessageDisruptorController(MessageDisruptorService messageDisruptorService) {
        this.messageDisruptorService = messageDisruptorService;
    }

    @PostMapping(path = "/disruptor", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> runMessageDisruptor(@RequestParam(name = "events", defaultValue = "1000") int events) {
        int published = messageDisruptorService.runDemo(events);
        Map<String, Object> payload = new HashMap<>();
        payload.put("requestedEvents", events);
        payload.put("publishedEvents", published);
        payload.put("messageTypes", Arrays.asList("ad", "news"));
        LOGGER.info("Handled /bench/message/disruptor, requestedLoops={}, publishedEvents={}", events, published);
        return payload;
    }
}
