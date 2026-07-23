package com.sporekart.bi.copilot.domain;

import java.time.YearMonth;

public record TrendDataPoint(YearMonth period, double value, String label) {}
