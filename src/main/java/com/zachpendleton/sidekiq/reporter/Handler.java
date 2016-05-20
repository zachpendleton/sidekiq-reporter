package com.zachpendleton.sidekiq.reporter;

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Receive and process requests from the Lambda environment.
 */
public class Handler {
    private static final Logger logger = Logger.getLogger(Handler.class);

    /**
     * Accept a scheduled event from Lambda and execute the application.
     *
     * @param event information about the scheduled event
     * @param context AWS Lambda context information
     */
    public void handle(ScheduledEvent event, Context context) {
        Properties properties = loadProperties();

        if (properties == null) {
            logger.error("unable to load configuration. check classpath for `reporter.properties`");
            logger.error("exiting");
            System.exit(1);
        }

        for (String queueName : properties.getProperty("sidekiq.queues").split(",")) {
            SidekiqQueue queue = new SidekiqQueue(queueName);
            long queueSize;

            try {
                queue.connect(properties.getProperty("sidekiq.host"), Integer.parseInt(properties.getProperty("sidekiq.port")));
                queueSize = queue.getSize();
            } finally {
                queue.disconnect();
            }

            if (configuredForGraphite(queueName, properties)) {
                Metric metric = new StatsdGauge(
                        properties.getProperty("sidekiq.queues." + queueName + ".graphite.key"),
                        properties.getProperty("graphite.host"),
                        Integer.parseInt(properties.getProperty("graphite.port")));
                metric.sendValue((double) queueSize);
            }

            if (configuredForCloudwatch(queueName, properties)) {
                Metric metric = new CloudwatchMetric(
                        properties.getProperty("sidekiq.queues." + queueName + ".cloudwatch.namespace"),
                        properties.getProperty("sidekiq.queues." + queueName + ".cloudwatch.name"));
                metric.sendValue((double) queueSize);
            }
        }
    }

    private Properties loadProperties() {
        try {
            Properties properties = new Properties();
            InputStream resourceStream = this.getClass().getResourceAsStream("reporter.properties");

            if (resourceStream == null) {
                throw new IOException();
            }

            properties.load(resourceStream);
            return properties;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean configuredForGraphite(String queueName, Properties properties) {
        String prefix = "sidekiq.queues." + queueName + ".graphite";

        return (properties.getProperty(prefix + ".key") != null &&
                properties.getProperty("graphite.host") != null &&
                properties.getProperty("graphite.port") != null);
    }

    private boolean configuredForCloudwatch(String queueName, Properties properties) {
        String prefix = "sidekiq.queues." + queueName + ".cloudwatch";

        return (properties.getProperty(prefix + ".namespace") != null &&
                properties.getProperty(prefix + ".metric") != null);
    }
}
