package cybertech.childguide;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 6/4/2013.
 * "https://fabiangare.000webhostapp.com/uploadInstalledApps.php"
 */
public class InstalledAppsConnection extends StringRequest {
    private static final String uploadAppURL = "https://fabiangare.000webhostapp.com/uploadInstalledApps.php";
    private static final String getAppURL = "https://fabiangare.000webhostapp.com/getInstalledApps.php";
    private static final String WORD_USED_URL = "https://fabiangare.000webhostapp.com/uploadUsedwords.php";
    Map<String, String> params;

    //sending installed Apps to the server.
    public InstalledAppsConnection(String ChildId, String appName, Response.Listener<String> listener) {
        super(Method.POST, uploadAppURL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildId);
        params.put("appname", appName);
    }

    // getting Apps froms online server.
    public InstalledAppsConnection(String ChildId, Response.Listener<String> listener) {
        super(Method.POST, getAppURL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildId);
    }

    // getting Apps froms online server.
    public InstalledAppsConnection(String ChildId, StringBuilder foundwords, String keyswords, Response.Listener<String> listener) {
        super(Method.POST, WORD_USED_URL, listener, null);
        params = new HashMap<>();
        params.put("childid", ChildId);
        params.put("keywords", keyswords);
        params.put("wordused", foundwords.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
