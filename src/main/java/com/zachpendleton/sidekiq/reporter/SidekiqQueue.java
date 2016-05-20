package com.zachpendleton.sidekiq.reporter;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * A Sidekiq queue with a name and a length.
 */
public class SidekiqQueue {
    private static final Logger logger = Logger.getLogger(SidekiqQueue.class);

    private Jedis connection;

    private final String name;

    SidekiqQueue(String name) {
        this.name = name;
    }

    /**
     * Connect to the Redis server hosting this queue.
     *
     * @param host the hostname of the Redis server
     * @param port the port the Redis server is listening on
     *
     * @return success boolean
     */
    public boolean connect(String host, int port) {
        return connect(host, port, 0);
    }

    /**
     * Connect to the Redis server hosting this queue.
     *
     * @param host the hostname of the Redis server
     * @param port the port the Redis server is listening on
     * @param databaseId the database ID containing Sidekiq information
     *
     * @return success boolean
     */
    public boolean connect(String host, int port, int databaseId) {
        connection = new Jedis(host, port);

        if (connection.isConnected()) {
            connection.select(databaseId);
        }

        return connection.isConnected();
    }

    /**
     * Disconnect from the Redis server.
     */
    public void disconnect() {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    /**
     * Fetch the queue size from the Redis server.
     *
     * @return the size of the queue
     */
    public long getSize() {
        if (connection == null || !connection.isConnected()) {
            logger.warn("attempted to access queue '" + queueKey() + "' without connecting to redis");
            return 0;
        }

        return connection.llen(queueKey());
    }

    /**
     * Construct the Redis key for this queue.
     *
     * @return a key name
     */
    private String queueKey() {
        return "queue:" + name;
    }
}
