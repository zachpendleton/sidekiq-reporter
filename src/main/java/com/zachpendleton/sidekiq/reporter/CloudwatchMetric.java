package com.zachpendleton.sidekiq.reporter;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import org.apache.log4j.Logger;

/**
 * A metric package for Cloudwatch
 */
public class CloudwatchMetric implements Metric {
    private static final AmazonCloudWatchClient client = new AmazonCloudWatchClient();

    private static final Logger logger = Logger.getLogger(CloudwatchMetric.class);

    private final String name;

    private final String namespace;

    private final String unit = "Count";

    /**
     *
     * @param namespace the namespace of the metric
     * @param name the name of the metric field
     */
    public CloudwatchMetric(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Send a datum object to this metric.
     *
     * @param value the measurement to record
     */
    public void sendValue(double value) {
        MetricDatum datum = new MetricDatum();
        datum.setMetricName(name);
        datum.setUnit(unit);
        datum.setValue(value);

        PutMetricDataRequest request = new PutMetricDataRequest();
        request.setNamespace(namespace);
        request.withMetricData(datum);

        client.putMetricData(request);
    }
}
