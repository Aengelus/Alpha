package at.aengelus.apps.stempeluhr;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sascha on 10/30/15.
 */
public class TimeTask {

    long start, end;
    // Statisch, da ein object einmal erzeugt werden soll und dann soll alles darauf zugreifen können.
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    static SimpleDateFormat hourDateFormat = new SimpleDateFormat("HH:mm:ss");

    public TimeTask() {
        start = new Date().getTime();
    }

    public TimeTask(double start, double end){
        this.start = (long) start;
        this.end = (long) end;
    }

    public void end() {
        end = new Date().getTime();
    }

//    public Date getDauer() {
//        // durch 1000, da milisekunden
//        Date date = new Date().getTime()-start - 60 * 60 * 1000);
//        return date;
//    }

    public String toString() {
        return simpleDateFormat.format(start) + " : " + hourDateFormat.format((end==0)?(new Date().getTime()-start)-60*60*1000:(end-start)-60*60*1000);
    }

    public String toString(double start, double end) {
        return simpleDateFormat.format(start) + " : " + hourDateFormat.format((end==0)?(new Date().getTime()-start)-60*60*1000:(end-start)-60*60*1000);
    }
}
