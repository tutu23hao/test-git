#!/usr/bin/env python3
"""Simple load generator for GET /bench/ping."""

import argparse
import concurrent.futures
import statistics
import time
import urllib.error
import urllib.request
from typing import Dict, List


def _worker(url: str, timeout: float, deadline: float) -> Dict[str, object]:
    successes = 0
    failures = 0
    latencies: List[float] = []

    while time.perf_counter() < deadline:
        start = time.perf_counter()
        try:
            with urllib.request.urlopen(url, timeout=timeout) as response:
                # Drain the body so the connection can be reused.
                response.read()
                if 200 <= response.status < 300:
                    successes += 1
                else:
                    failures += 1
        except urllib.error.URLError:
            failures += 1
        except Exception:
            failures += 1
        finally:
            latencies.append((time.perf_counter() - start) * 1000)
    return {"success": successes, "failure": failures, "latencies": latencies}


def run(url: str, concurrency: int, duration: float, timeout: float) -> None:
    stop_at = time.perf_counter() + duration
    futures: List[concurrent.futures.Future] = []
    with concurrent.futures.ThreadPoolExecutor(max_workers=concurrency) as executor:
        for _ in range(concurrency):
            futures.append(executor.submit(_worker, url, timeout, stop_at))

    total_success = 0
    total_failure = 0
    collected_latencies: List[float] = []
    for future in futures:
        result = future.result()
        total_success += result["success"]
        total_failure += result["failure"]
        collected_latencies.extend(result["latencies"])

    total_requests = total_success + total_failure

    stats = {
        "total_requests": total_requests,
        "successes": total_success,
        "failures": total_failure,
        "requests_per_second": total_requests / duration if duration else 0,
        "avg_latency_ms": statistics.mean(collected_latencies) if collected_latencies else 0,
        "p95_latency_ms": statistics.quantiles(collected_latencies, n=100)[94]
        if len(collected_latencies) >= 100
        else 0,
    }

    print("Load test complete:")
    for key, value in stats.items():
        print(f"  {key}: {value:.2f}" if isinstance(value, float) else f"  {key}: {value}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Load test /bench/ping endpoint")
    parser.add_argument("--url", default="http://localhost:8080/bench/ping", help="Target URL")
    parser.add_argument("--concurrency", type=int, default=20, help="Number of worker threads")
    parser.add_argument("--duration", type=float, default=30.0, help="Test duration in seconds")
    parser.add_argument(
        "--timeout", type=float, default=5.0, help="HTTP client timeout per request"
    )
    args = parser.parse_args()

    run(args.url, args.concurrency, args.duration, args.timeout)
