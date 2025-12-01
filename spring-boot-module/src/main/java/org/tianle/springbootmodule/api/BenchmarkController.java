package org.tianle.springbootmodule.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Tiny REST endpoint with negligible logic for load testing the servlet container.
 */
@RestController
@RequestMapping(path = "/bench")
public class BenchmarkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkController.class);

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> benchmarkPing() {
        long start = System.nanoTime();
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "ok");
        long elapsedMicros = (System.nanoTime() - start) / 1_000;
//        LOGGER.info("Handled /bench/ping in {} microseconds", elapsedMicros);
        return payload;
    }
}
