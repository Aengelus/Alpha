package com.mappau.apps.digitaltimestampclock;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    final static ArrayList<TimeTask> list = new ArrayList<TimeTask>();
    static ArrayAdapter arrayAdapter = null;
    static Timer timer = null;
    static FloatingActionButton fab = null;

    static boolean time_running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if(!time_running)
        {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp, getApplicationContext().getTheme()));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
        else
        {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_24dp, getApplicationContext().getTheme()));
            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!time_running)
                {
                    time_running = true;
                    list.add(0,new TimeTask());
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp, getApplicationContext().getTheme()));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                }
                else
                {
                    list.get(0).end();
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_24dp, getApplicationContext().getTheme()));
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    time_running = false;

                }

                arrayAdapter.notifyDataSetChanged();
            }
        });

        ListView listview  = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<TimeTask>(this,R.layout.list_item,R.id.itemText,list);



        listview.setAdapter(arrayAdapter);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        },0,1000);



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
}
