package org.tianle.springbootmodule.api;

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

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> benchmarkPing() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "ok");
        return payload;
    }
}
