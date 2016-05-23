package com.zachpendleton.sidekiq.reporter;

interface Metric {
    void sendValue(double value);
}
