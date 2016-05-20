package com.zachpendleton.sidekiq.reporter;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Scheduled Lambda event data wrapper
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledEvent {
    @JsonProperty
    private String account;

    @JsonProperty("detail-type")
    private String detailType;

    @JsonProperty
    private String id;

    @JsonProperty
    private String region;

    @JsonProperty
    private String[] resources;

    @JsonProperty
    private String source;

    @JsonProperty
    private String time;

    public String getAccount() {
        return account;
    }

    public String getDetailType() {
        return detailType;
    }

    public String getId() {
        return id;
    }

    public String getRegion() {
        return region;
    }

    public String[] getResources() {
        return resources;
    }

    public String getSource() {
        return source;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.join(":", new String[] { region, id, source });
    }
}
