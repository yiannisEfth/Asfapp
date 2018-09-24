package ltd.solutions.software.myt.asfapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String yourDate = "24/09/2018";
        String yourHour = "20:29:23";
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        Date todayTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        date.format(todayDate);
        DateFormat hour = new SimpleDateFormat("HH:mm:ss");
        hour.format(todayTime);
        if (todayDate.equals(yourDate) && todayTime.equals(yourHour)) {
            createNotification(context, "Times up", "5 Seconds have passed", "Alert");
        }
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {
        //Checks if api is 26 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Creates a notification popup for the user to see
            NotificationChannel channel = new NotificationChannel("2", "Notification channel", NotificationManager.IMPORTANCE_DEFAULT);
            //sets all the information of the popup
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "2")
                    .setContentTitle(msg)
                    .setContentText(msgText)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(31234, mBuilder.build());
        } else {
            //for api below 26 uses this code to create the notification because channels are only
            //available for api 26 and above
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle("A point of interest is nearby!");
            mBuilder.setContentText("You are very close to " + ".");
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(31234, mBuilder.build());

        }




    }
}