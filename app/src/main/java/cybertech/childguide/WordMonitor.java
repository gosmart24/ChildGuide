package cybertech.childguide;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WordMonitor extends AppCompatActivity {

    ListView baseView;
    List<wordMonitorModel> listStorage;
    String childID;
    SharedPreferences sp;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_monitor);
        baseView = (ListView) findViewById(R.id.wordMBaseView);
        listStorage = null;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        childID = sp.getString("childid", null);
        new loadMonitorWords().execute();
        baseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBody(listStorage.get(position).getBody());
            }
        });

    }

    private void showBody(String body) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WordMonitor.this);
        builder.setTitle("Text Body");
        builder.setMessage(body);
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progress.dismiss();
    }

    private class loadMonitorWords extends AsyncTask<Void, Void, Void> {
        WordMonitorAdapter adapter;
        String[] wordsUsed;
        ArrayList<String> searchlist = new ArrayList<>();
        ArrayList<String> smslist = new ArrayList<>();
        ArrayList<String> smsDatelist = new ArrayList<>();
        ArrayList<String> siteDatelist = new ArrayList<>();
        String[] keywords;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Thread", "onPreExecute");
            progress = ProgressDialog.show(WordMonitor.this, null, "loading please wait...", true, true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            baseView.setAdapter(adapter);
            Log.i("Thread", "OnPostExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            listStorage = new ArrayList<>();
            Double monitpr = 0.0;
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Thread", "Response : " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            JSONArray jsonArray = jsonObject.getJSONArray("usedwords");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject usedWord = jsonArray.getJSONObject(i);
                                keywords = usedWord.getString("keywords").split(",");
                                wordsUsed = usedWord.getString("wordused").split("______");
                                for (int j = 0; j < wordsUsed.length; j++) {
                                    String word = wordsUsed[j];
                                    if (word.startsWith("search")) {
                                        String searchWord = word.substring(6);
                                        searchlist.add(searchWord);
                                    } else if (word.startsWith("sms_inbox") || word.startsWith("sms_sent") || word.startsWith("sms_draft")) {
                                        if (word.startsWith("sms_inbox")) {
                                            listStorage.add(new wordMonitorModel());
                                            String typedWord = "SMS TYPE : INBOX\n" + word.substring(9);
                                            smslist.add(typedWord);
                                        } else if (word.startsWith("sms_sent")) {
                                            String typedWord = "SMS TYPE : SENT\n" + word.substring(8);
                                            smslist.add(typedWord);
                                        } else if (word.startsWith("sms_draft")) {
                                            String typedWord = "SMS TYPE : DRAFT\n" + word.substring(9);
                                            smslist.add(typedWord);
                                        }
                                    } else if (word.startsWith("smsdate")) {
                                        smsDatelist.add(word.substring(7));
                                    } else if (word.startsWith("sitedate")) {
                                        siteDatelist.add(word.substring(8));
                                    }
                                }
                                if (searchlist.size() > 0) {
                                    for (int n = 0; n < searchlist.size(); n++) {
                                        for (String key : keywords) {
                                            if (searchlist.get(i).contains(key)) {
                                                listStorage.add(new wordMonitorModel(key, "Search", siteDatelist.get(n), searchlist.get(n)));
                                            }
                                        }

                                    }
                                }
                                if (smslist.size() > 0) {
                                    for (int n = 0; n < smslist.size(); n++) {
                                        for (String key : keywords) {
                                            if (smslist.get(i).contains(key)) {
                                                listStorage.add(new wordMonitorModel(key, "Text", smsDatelist.get(n), smslist.get(n)));
                                            }
                                        }

                                    }
                                }
                            }

                            if (listStorage.size() > 0) {
                                adapter.notifyDataSetChanged();
                                progress.dismiss();
                            } else {
                                progress.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(WordMonitor.this);
                                builder.setTitle("Monitor Alert");
                                builder.setMessage("There is no record of monitor words from your child");
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(WordMonitor.this, ParentHome.class));
                                    }
                                });
                                builder.show();
                            }

                        } else {
                            DialogInterface.OnClickListener lisener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(WordMonitor.this, ParentHome.class));
                                }
                            };
                            MyDialog.dialogOK(WordMonitor.this, jsonObject.getString("message"), lisener);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Log.i("Thread", "starting connections");
            ParentConnection connection = new ParentConnection(childID, monitpr, listener);
            RequestQueue queue = Volley.newRequestQueue(WordMonitor.this);
            queue.add(connection);
            adapter = new WordMonitorAdapter(getApplicationContext(), listStorage);
            return null;
        }
    }
}
