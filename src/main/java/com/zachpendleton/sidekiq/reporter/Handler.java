package com.zachpendleton.sidekiq.reporter;

import com.amazonaws.services.lambda.runtime.Context;
import org.apache.log4j.Logger;

/**
 * Receive and process requests from the Lambda environment.
 */
public class Handler {
    private static Logger logger = Logger.getLogger(Handler.class);

    public void handle(ScheduledEvent event, Context context) {

    }
}
