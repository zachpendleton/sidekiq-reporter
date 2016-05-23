package com.zachpendleton.sidekiq.reporter;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class StatsdGauge implements Metric {
    private static final Logger logger = Logger.getLogger(StatsdGauge.class);

    private InetAddress address;

    private String name;

    private int port;

    /**
     * @param name the full path to the graphite metric being tracked
     */
    public StatsdGauge(String name, String hostname, int port) {
        try {
            this.address = InetAddress.getByName(hostname);
            this.name = name;
            this.port = port;
        } catch (UnknownHostException e) {
            logger.error("unknown host: " + hostname);
        }
    }

    /**
     * Send a measurement to the configured StatsD server
     *
     * @param value the measurement to record
     */
    public void sendValue(double value) {
        if (address == null || name == null) {
            logger.warn("attempting to send statsd message without a configured host");
            return;
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(valueToPacket(value),
                    valueToPacket(value).length,
                    address,
                    port);

            socket.send(packet);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Translate a metric value into a StatsD packet
     *
     * @param value the measurement to record
     */
    private byte[] valueToPacket(double value) {
        return String.format("%s:%.2f|g", name, value).getBytes(StandardCharsets.UTF_8);
    }
}
