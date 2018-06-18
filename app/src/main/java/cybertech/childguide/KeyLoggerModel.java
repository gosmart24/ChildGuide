package cybertech.childguide;

import java.util.Date;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 */
public class KeyLoggerModel {
    String eventType;
    String eventDate;
    String eventBody;
    Date eventsDate;

    public KeyLoggerModel(String eventType, Date eventsDate, String eventBody) {
        this.eventType = eventType;
        this.eventsDate = eventsDate;
        this.eventBody = eventBody;
    }

    public KeyLoggerModel(String eventType, String eventDate, String eventBody) {
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventBody = eventBody;
    }

    public KeyLoggerModel() {
    }

    public Date getEventsDate() {
        return eventsDate;
    }

    public void setEventsDate(Date eventsDate) {
        this.eventsDate = eventsDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventBody() {
        return eventBody;
    }

    public void setEventBody(String eventBody) {
        this.eventBody = eventBody;
    }
}
