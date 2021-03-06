package com.owl.crazynote;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InstanceNotification {
    private String dateToSet;
    private Context context;
    private AlarmManager alarmManager;
    private String titleNotification;
    private String contentNotification;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.UK);
    private long delay;
    private PendingIntent pendingIntent;
    private int id;
    private boolean notificationUP =false;

    public InstanceNotification(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        setContext(context);
    }
    public void setDateToSet(String dateToSet) {
        this.dateToSet = dateToSet;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setContentNotification(String contentNotification) {
        this.contentNotification = contentNotification;
    }
//    public void setTitleNotification(String titleNotification) {
//        this.titleNotification = titleNotification;
//    }

    public boolean isNotificationUP(){
        return notificationUP;
    }

    private Notification notificationBuilder() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Crazynote");
        builder.setContentText(contentNotification);
        builder.setSmallIcon(R.drawable.ic_done_all_white_36dp);
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        return builder.build();
    }
    private void convertStringToMillis() throws ParseException{
        Date date =    dateFormat.parse(dateToSet);
        long millis = date.getTime();
        Calendar calendar = Calendar.getInstance();
        long millis2 = calendar.getTimeInMillis();
        delay =  millis - millis2;
    }

    public void scheduleNotification(int id) {
        this.id = id;
        try {
            convertStringToMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent notificationIntent = new Intent(context , NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID,id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notificationBuilder());
        notificationIntent.setAction("NotificationUniqueName"+id);
        pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        notificationUP = true;
        Log.d("NotificationTest","Notification is set or is set again.");
    }
    public void cancelNotification(){
        Intent notificationIntent = new Intent(context , NotificationPublisher.class);
        notificationIntent.setAction("NotificationUniqueName"+id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        notificationUP = false;
        Log.d("NotificationTest","Notification is canceled");
    }
//    public boolean notificationUP() throws NullPointerException{
//        return PendingIntent.getBroadcast(context,0, new Intent(context,NotificationPublisher.class),PendingIntent.FLAG_NO_CREATE) != null;
//    }


}
