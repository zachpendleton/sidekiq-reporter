package com.zachpendleton.sidekiq.reporter;

/**
 * Created by zachp on 5/20/16.
 */
public interface Metric {
    void sendValue(double value);
}
