package cybertech.childguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 * https://fabiangare.000webhostapp.com/getInstalledApps.php
 * https://fabiangare.000webhostapp.com/getkeyevents.php
 * https://fabiangare.000webhostapp.com/getcallLogs.php
 */
public class ParentConnection extends StringRequest {
    private static final String CHECK_ID_URL = "https://fabiangare.000webhostapp.com/checkId.php";
    private static final String Browser_History_URL = "https://fabiangare.000webhostapp.com/getbrowserhistory.php";
    private static final String Locations_URL = "https://fabiangare.000webhostapp.com/getLocations.php";
    private static final String CALL_LOGS_URL = "https://fabiangare.000webhostapp.com/getcallLogs.php";
    private static final String Word_MONITOR_URL = "https://fabiangare.000webhostapp.com/getusedwords.php";
    private static final String Key_Events_URL = "https://fabiangare.000webhostapp.com/getevents.php";
    Map<String, String> params;

    // constructor for checking valid childID online.
    public ParentConnection(String trackeringID, Response.Listener<String> listener) {
        super(Method.POST, CHECK_ID_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
    }

    // locations from server.
    public ParentConnection(String trackeringID, String limit, Response.Listener<String> listener) {
        super(Method.POST, Locations_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
        params.put("limit", limit);
    }

    // Call logs from server.
    public ParentConnection(String trackeringID, int duration, Response.Listener<String> listener) {
        super(Method.POST, CALL_LOGS_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
    }

    // Word monitor from server.
    public ParentConnection(String trackeringID, Double monitor, Response.Listener<String> listener) {
        super(Method.POST, Word_MONITOR_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
    }

    // Browser history.
    public ParentConnection(String trackeringID, long Browser, Response.Listener<String> listener) {
        super(Method.POST, Browser_History_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
    }

    // Key Events from server.
    public ParentConnection(String trackeringID, boolean events, Response.Listener<String> listener) {
        super(Method.POST, Key_Events_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", trackeringID);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
