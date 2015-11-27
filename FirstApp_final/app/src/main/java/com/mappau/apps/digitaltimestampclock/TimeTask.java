package com.mappau.apps.digitaltimestampclock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by guru on 29.10.2015.
 */
public class TimeTask {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public long start, end;

    public TimeTask(){
        start = new Date().getTime();
    }

    public void end(){
        end =  new Date().getTime();
    }

    public String toString(){
        return sdf.format(start) +" - "+ format((end==0)?(new Date().getTime()-start):(end-start));
    }

    public String format(long millis){

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }


}
