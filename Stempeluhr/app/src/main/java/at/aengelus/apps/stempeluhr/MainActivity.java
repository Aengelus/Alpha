package at.aengelus.apps.stempeluhr;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Schriften, die immer die selbe Breite haben => Monospace
    // Save elements in arrayList   | Durch statische Methoden wird es global zugänglich
    static private ArrayList<TimeTask> arrayList = new ArrayList<>();
    static private ArrayAdapter<TimeTask> arrayAdapter;
    // static private TimeTask timeTask;
    static private String backendapi;
    static private String user;
    static private JSONArray sessions;
    private long currentTime;

    private FloatingActionButton fab;

    static private boolean timeRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backendapi = "http://tic.mappau.com";
        user = "alpha";

//        Log.d("Stempeluhr", "onCreate");

        // Create a Toast
        Toast.makeText(getBaseContext(), "Hallo app", Toast.LENGTH_SHORT).show();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // With 0 as a first parameter it will place the listItem in the first place
//                arrayAdapter.notifyDataSetChanged();
                if (!timeRunning) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp, getApplicationContext().getTheme()));
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    //arrayList.add(0, new TimeTask());
                    start();
                    //talkToRest("start");
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_24dp, getApplicationContext().getTheme()));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    //arrayList.get(0).end();
                    end();
                    //talkToRest("end");
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        // Take the listView by id
        ListView listView = (ListView) findViewById(R.id.listView);

        // Use an adapter
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.textView, arrayList);

        // Set the listView Adapter
        listView.setAdapter(arrayAdapter);

        // This creates a timer object, which will increment the time every second.
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 1000);

        // This will initially fetch the data
        session();
        //talkToRest("sessions");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Custom Methods for talking with the rest api
// Custom Methods for talking with the rest api
    public void start() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;

                try {
                    // Set the url to the rest api level
                    URL url = null;
                    url = new URL(backendapi + "/" + user + "/start");

                    // Connect to the url
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // This will not save the cache
                    urlConnection.setUseCaches(false);
                    // BE CAREFUL, IF YOU TRY TO USE POST WHEN THERE IS A GET METHOD IT WILL NEVER WORK
                    // Sets the request method for the rest api
                    urlConnection.setRequestMethod("POST");
                    // get the content
                    urlConnection.getContent();
                    session();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return null;
            }

            @Override
            // THIS WILL RUN ON THE UI THREAD, BE CAREFUL OF HEAVY LOAD
            protected void onPostExecute(Void aVoid) {
                updateList();
                super.onPostExecute(aVoid);
            }
        };

        // Start the asynchrone task
        task.execute();
    }

    // Custom Methods for talking with the rest api
    public void end() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;

                try {
                    // Set the url to the rest api level
                    URL url = null;
                    url = new URL(backendapi + "/" + user + "/end");

                    // Connect to the url
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // This will not save the cache
                    urlConnection.setUseCaches(false);
                    // BE CAREFUL, IF YOU TRY TO USE POST WHEN THERE IS A GET METHOD IT WILL NEVER WORK
                    // Sets the request method for the rest api
                    urlConnection.setRequestMethod("POST");
                    // get the content
                    urlConnection.getContent();
                    session();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return null;
            }

            @Override
            // THIS WILL RUN ON THE UI THREAD, BE CAREFUL OF HEAVY LOAD
            protected void onPostExecute(Void aVoid) {
                updateList();
                super.onPostExecute(aVoid);
            }
        };

// Start the asynchrone task
        task.execute();
    }

    // Custom Methods for talking with the rest api
    public void session() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;

                try {
                    // Set the url to the rest api level
                    URL url = null;
                    url = new URL(backendapi + "/" + user + "/sessions");

                    // Connect to the url
                    urlConnection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr = null;
                    while ((inputStr = reader.readLine()) != null) {
                        responseStrBuilder.append(inputStr);
                    }

                    JSONObject obj = new JSONObject(responseStrBuilder.toString());
                    currentTime = obj.getLong("currentTime");
                    currentTime = currentTime - new Date().getTime();
                    sessions = obj.getJSONArray("sessions");

                } catch (JSONException je) {
                    je.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return null;
            }

            @Override
// THIS WILL RUN ON THE UI THREAD, BE CAREFUL OF HEAVY LOAD
            protected void onPostExecute(Void aVoid) {
                updateList();
                super.onPostExecute(aVoid);
            }
        };

        // Start the asynchrone task
        task.execute();
    }


/*    public void talkToRest(final String startEndSession){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;


                try {
                    // Set the url to the rest api level
                    URL url = null;
                    if(startEndSession.equals("end")){
                        url = new URL(backendapi+"/"+user+"/end");
                    } else if(startEndSession.equals("start")){
                        url = new URL(backendapi+"/"+user+"/start");
                    } else if (startEndSession.equals("sessions")){
                        url = new URL(backendapi+"/"+user+"/sessions");
                    }

                    if(url != null){
                        // Connect to the url
                        urlConnection = (HttpURLConnection) url.openConnection();

                        //if (startEndSession.equals("sessions")){
                            //urlConnection.setRequestMethod("GET");
                            // Get the json object data
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(urlConnection.getInputStream()));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr = null;
                            while ((inputStr = reader.readLine()) != null){
                                responseStrBuilder.append(inputStr);
                            }

                            JSONObject obj = new JSONObject(responseStrBuilder.toString());
                            sessions = obj.getJSONArray("sessions");
                        //} else
                        if(startEndSession.equals("end") || startEndSession.equals("start")){
                            // This will not save the cache
                            urlConnection.setUseCaches(false);
                            // BE CAREFUL, IF YOU TRY TO USE POST WHEN THERE IS A GET METHOD IT WILL NEVER WORK
                            // Sets the request method for the rest api
                            urlConnection.setRequestMethod("POST");
                            // get the content
                            urlConnection.getContent();
                        }
                    }

                } catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException je){
                    je.printStackTrace();
                } finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
                return null;
            }

            @Override
            // THIS WILL RUN ON THE UI THREAD, BE CAREFUL OF HEAVY LOAD
            protected void onPostExecute(Void aVoid){
                if(sessions != null){
                    updateList();
                }
                super.onPostExecute(aVoid);
            }
        };

        // Start the asynchrone task
        task.execute();
    }*/

    public void updateList() {
        // this.sessions

        arrayAdapter.clear();
        timeRunning = false;

        for (int i = sessions.length() - 1; i > 0; i--) {
            try {
                double start = sessions.getJSONObject(i).getDouble("start");
                //double end = sessions.getJSONObject(i).getDouble("end");

                // This will create the timestring
                // String data = time.toString(start, end);

                TimeTask time = null;
                if (sessions.getJSONObject(i).has("end")) {
                    time = new TimeTask(start, sessions.getJSONObject(i).getDouble("end"));
                } else {
                    time = new TimeTask(start-currentTime, 0);
                    timeRunning = true;
                }

                // Display the text on the screen
                arrayAdapter.add(time);

                //Toast.makeText(getBaseContext(), time.toString(start, end), Toast.LENGTH_LONG).show();


            } catch (JSONException js) {
                js.printStackTrace();
            }


        /*    try {
                Toast.makeText(getBaseContext(), String.valueOf(sessions.getJSONObject(0).getDouble("start")), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } */
        }

        if (timeRunning) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp, getApplicationContext().getTheme()));
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_24dp, getApplicationContext().getTheme()));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
