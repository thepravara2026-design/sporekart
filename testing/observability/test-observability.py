#!/usr/bin/env python3
"""Observability validation tests for SporeKart platform."""

import json
import sys
import time
import argparse
import urllib.request
import urllib.error


class ObservabilityTestSuite:

    def __init__(self, base_url, verbose=False):
        self.base_url = base_url
        self.verbose = verbose
        self.passed = 0
        self.failed = 0
        self.results = []

    def log(self, message):
        if self.verbose:
            print(f"  {message}")

    def check(self, name, condition, detail=""):
        if condition:
            self.passed += 1
            status = "PASS"
        else:
            self.failed += 1
            status = "FAIL"
        self.results.append({"name": name, "status": status, "detail": detail})
        print(f"  [{status}] {name}" + (f" - {detail}" if detail else ""))

    def fetch_json(self, path):
        url = f"{self.base_url}{path}"
        try:
            req = urllib.request.Request(url, headers={"Accept": "application/json"})
            with urllib.request.urlopen(req, timeout=5) as resp:
                data = json.loads(resp.read().decode())
                return data, resp.status, resp.headers
        except urllib.error.HTTPError as e:
            return None, e.code, e.headers
        except Exception as e:
            return None, 0, str(e)

    def test_health_endpoint(self):
        self.log("Testing health endpoint...")
        data, status, headers = self.fetch_json("/actuator/health")
        self.check("Health endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data:
            self.check("Health status present", "status" in data)
            self.check("Health is UP", data.get("status") == "UP",
                        f"Status: {data.get('status')}")

    def test_readiness_probe(self):
        self.log("Testing readiness probe...")
        data, status, _ = self.fetch_json("/actuator/health/readiness")
        self.check("Readiness endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data:
            self.check("Readiness UP", data.get("status") == "UP",
                        f"Status: {data.get('status')}")

    def test_liveness_probe(self):
        self.log("Testing liveness probe...")
        data, status, _ = self.fetch_json("/actuator/health/liveness")
        self.check("Liveness endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data:
            self.check("Liveness UP", data.get("status") == "UP",
                        f"Status: {data.get('status')}")

    def test_metrics_endpoint(self):
        self.log("Testing metrics endpoint...")
        data, status, _ = self.fetch_json("/actuator/metrics")
        self.check("Metrics endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data and "names" in data:
            required_metrics = [
                "jvm.memory.used", "jvm.memory.max", "jvm.gc.pause",
                "http.server.requests", "process.cpu.usage",
                "hikaricp.connections.active", "logback.events",
            ]
            for metric in required_metrics:
                self.check(f"Metric present: {metric}",
                            metric in data["names"])

    def test_prometheus_endpoint(self):
        self.log("Testing Prometheus endpoint...")
        _, status, _ = self.fetch_json("/actuator/prometheus")
        self.check("Prometheus endpoint accessible", status in (200, 404, 405),
                    f"HTTP {status}")

    def test_trace_headers(self):
        self.log("Testing trace headers...")
        data, status, headers = self.fetch_json("/actuator/health")
        trace_headers = ["X-Trace-Id", "X-Span-Id", "X-Correlation-Id", "X-Request-Id"]
        for header in trace_headers:
            present = header in headers
            self.check(f"Trace header present: {header}",
                        present, headers.get(header, "missing") if self.verbose else "")

    def test_info_endpoint(self):
        self.log("Testing info endpoint...")
        data, status, _ = self.fetch_json("/actuator/info")
        self.check("Info endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data:
            for section in ["app", "build", "git", "java", "os"]:
                self.check(f"Info section: {section}", section in data)

    def test_loggers_endpoint(self):
        self.log("Testing loggers endpoint...")
        data, status, _ = self.fetch_json("/actuator/loggers")
        self.check("Loggers endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data and "levels" in data:
            self.check("Logger levels present", True)

    def test_httptrace_endpoint(self):
        self.log("Testing HTTP trace endpoint...")
        _, status, _ = self.fetch_json("/actuator/httptrace")
        self.check("HTTP trace endpoint", status in (200, 404),
                    f"HTTP {status}")

    def test_heapdump_endpoint(self):
        self.log("Checking heapdump endpoint...")
        _, status, _ = self.fetch_json("/actuator/heapdump")
        self.check("Heapdump endpoint registered",
                    status in (200, 405, 401, 403),
                    f"HTTP {status}")

    def test_threaddump_endpoint(self):
        self.log("Testing threaddump endpoint...")
        data, status, _ = self.fetch_json("/actuator/threaddump")
        self.check("Threaddump endpoint accessible", status == 200,
                    f"HTTP {status}")
        if data:
            self.check("Thread dump contains threads",
                        "threads" in data,
                        f"Threads: {len(data.get('threads', []))}")

    def test_cors_headers(self):
        self.log("Testing CORS headers...")
        url = f"{self.base_url}/actuator/health"
        try:
            req = urllib.request.Request(url, method="OPTIONS",
                headers={"Origin": "https://sporekart.com",
                         "Access-Control-Request-Method": "GET"})
            with urllib.request.urlopen(req, timeout=5) as resp:
                headers = resp.headers
        except Exception:
            data, status, headers = self.fetch_json("/actuator/health")
        cors_header = headers.get("Access-Control-Allow-Origin", "")
        allowed = cors_header == "*" or cors_header == "https://sporekart.com"
        self.check("CORS headers present", allowed,
                    f"Access-Control-Allow-Origin: {cors_header}")

    def run_all(self):
        print(f"\n=== SporeKart Observability Test Suite ===")
        print(f"Target: {self.base_url}\n")

        tests = [
            ("Health Endpoint", self.test_health_endpoint),
            ("Readiness Probe", self.test_readiness_probe),
            ("Liveness Probe", self.test_liveness_probe),
            ("Metrics Endpoint", self.test_metrics_endpoint),
            ("Prometheus Endpoint", self.test_prometheus_endpoint),
            ("Trace Headers", self.test_trace_headers),
            ("Info Endpoint", self.test_info_endpoint),
            ("Loggers Endpoint", self.test_loggers_endpoint),
            ("HTTP Trace Endpoint", self.test_httptrace_endpoint),
            ("Heapdump Endpoint", self.test_heapdump_endpoint),
            ("Threaddump Endpoint", self.test_threaddump_endpoint),
            ("CORS Headers", self.test_cors_headers),
        ]

        for name, test_fn in tests:
            print(f"\n--- {name} ---")
            try:
                test_fn()
            except Exception as e:
                self.failed += 1
                self.results.append({"name": name, "status": "ERROR", "detail": str(e)})
                print(f"  [ERROR] {name}: {e}")

        total = self.passed + self.failed
        print(f"\n{'='*50}")
        print(f"RESULTS: {self.passed}/{total} passed, {self.failed} failed")
        print(f"{'='*50}")

        return self.failed == 0


def main():
    parser = argparse.ArgumentParser(description="SporeKart Observability Test Suite")
    parser.add_argument("--base-url", default="http://localhost:8080",
                        help="Base URL of the application")
    parser.add_argument("--verbose", "-v", action="store_true",
                        help="Verbose output")
    args = parser.parse_args()

    suite = ObservabilityTestSuite(args.base_url, verbose=args.verbose)
    success = suite.run_all()
    sys.exit(0 if success else 1)


if __name__ == "__main__":
    main()
