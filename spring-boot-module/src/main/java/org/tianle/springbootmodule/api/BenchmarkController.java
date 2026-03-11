package org.tianle.springbootmodule.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tianle.springbootmodule.disruptor.service.DisruptorDemoService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Tiny REST endpoint with negligible logic for load testing the servlet container.
 */
@RestController
@RequestMapping(path = "/bench")
public class BenchmarkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkController.class);

    private final DisruptorDemoService disruptorDemoService;

    public BenchmarkController(DisruptorDemoService disruptorDemoService) {
        this.disruptorDemoService = disruptorDemoService;
    }

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> benchmarkPing() {
        long start = System.nanoTime();
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "ok");
        long elapsedMicros = (System.nanoTime() - start) / 1_000;
        LOGGER.info("Handled /bench/ping in {} microseconds", elapsedMicros);
        return payload;
    }

    @PostMapping(path = "/disruptor", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> runDisruptorDemo(@RequestParam(name = "events", defaultValue = "1000") int events) {
        int published = disruptorDemoService.runDemo(events);
        Map<String, Object> payload = new HashMap<>();
        payload.put("requestedEvents", events);
        payload.put("publishedEvents", published);
        LOGGER.info("Handled /bench/disruptor in {} microseconds", published);
        return payload;
    }

    public static void main(String[] args) {
        String[] testCases = {
                "abbc",    // 应该：占有优先失败，贪婪成功
                "abbbc",   // 应该：都成功
                "abc",     // 应该：都失败（b数量不足）
                "abbbbc"   // 应该：都失败（b数量过多）
        };

        String regexPossessive = "ab{1,3}+bc";
        String regexGreedy = "ab{1,3}bc";

        System.out.println("占有优先: " + regexPossessive);
        System.out.println("普通贪婪: " + regexGreedy);
        System.out.println("=" .repeat(50));

        System.out.printf("%-8s | %-12s | %-12s%n",
                "文本", "占有优先", "普通贪婪");
        System.out.println("-".repeat(40));

        for (String text : testCases) {
            boolean possessiveResult = Pattern.matches(regexPossessive, text);
            boolean greedyResult = Pattern.matches(regexGreedy, text);

            System.out.printf("%-8s | %-12s | %-12s%n",
                    text,
                    possessiveResult ? "✅ 成功" : "❌ 失败",
                    greedyResult ? "✅ 成功" : "❌ 失败");
        }
    }
}
