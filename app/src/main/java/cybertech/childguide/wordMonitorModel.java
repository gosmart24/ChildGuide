package cybertech.childguide;

import java.util.Date;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 */
public class wordMonitorModel {
    String wordUsed;
    String wordUsedIn;
    String wordUsedDate;
    String body;

    public wordMonitorModel(String wordUsed, String wordUsedIn, String wordUsedDate, String body) {
        this.wordUsed = wordUsed;
        this.wordUsedIn = wordUsedIn;
        this.wordUsedDate = wordUsedDate;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public wordMonitorModel(String wordUsed, String wordUsedIn, Date wordUsedDates) {
        this.wordUsed = wordUsed;
        this.wordUsedIn = wordUsedIn;
        this.wordUsedDates = wordUsedDates;
    }

    public Date getWordUsedDates() {
        return wordUsedDates;
    }

    public void setWordUsedDates(Date wordUsedDates) {
        this.wordUsedDates = wordUsedDates;
    }

    Date wordUsedDates;


    public wordMonitorModel(String wordUsed, String wordUsedIn, String wordUsedDate) {
        this.wordUsed = wordUsed;
        this.wordUsedIn = wordUsedIn;
        this.wordUsedDate = wordUsedDate;
    }

    public wordMonitorModel() {
    }

    public String getWordUsed() {
        return wordUsed;
    }

    public void setWordUsed(String wordUsed) {
        this.wordUsed = wordUsed;
    }

    public String getWordUsedIn() {
        return wordUsedIn;
    }

    public void setWordUsedIn(String wordUsedIn) {
        this.wordUsedIn = wordUsedIn;
    }

    public String getWordUsedDate() {
        return wordUsedDate;
    }

    public void setWordUsedDate(String wordUsedDate) {
        this.wordUsedDate = wordUsedDate;
    }
}
