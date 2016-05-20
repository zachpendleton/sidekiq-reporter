# Sidekiq Reporter

Sidekiq Reporter is a Sidekiq queue-size reporter designed to run inside a
scheduled AWS Lambda function.

It publishes metric data to StatsD, AWS Cloudwatch, or both, and can publish
sizes from multiple queues.

## Configuration

Sidekiq Reporter is configured by a `reporter.properties` file on the classpath.

```ini
# declare followed queues as a comma-separated list
sidekiq.queues=default,urgent

# publish default queue size to graphite and cloudwatch
sidekiq.queues.default.graphite.key=app.sidekiq.queue.default.size
sidekiq.queues.default.cloudwatch.namespace=app
sidekiq.queues.default.cloudwatch.metric=QueueSize_Default

# publish urgent queue info only to cloudwatch
sidekiq.queues.urgent.cloudwatch.namespace=app
sidekiq.queues.urgent.cloudwatch.metric=QueueSize_Urgent
```

## Building

1. Clone the project: `git clone https://github.com/zachpendleton/sidekiq-reporter`
2. Add a `reporter.properties` file at `src/main/resources` and add your
configuration as described above.
3. Build the project as an uberjar by running `./gradlew shadowJar`
4. Upload the JAR to Lambda and configure it as a scheduled job (TODO: link to documentation).

## Contributing

TODO