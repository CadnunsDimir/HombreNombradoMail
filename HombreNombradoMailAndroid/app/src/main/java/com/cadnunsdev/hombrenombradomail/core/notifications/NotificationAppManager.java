package com.cadnunsdev.hombrenombradomail.core.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.cadnunsdev.hombrenombradomail.EmailContentAct;
import com.cadnunsdev.hombrenombradomail.EmailFrag;
import com.cadnunsdev.hombrenombradomail.MainAct;
import com.cadnunsdev.hombrenombradomail.R;

import java.util.Calendar;

/**
 * Created by Tiago Silva on 02/02/2017.
 */

public class NotificationAppManager {


    private static final String FRAGMENT_NAME = "FRAGMENT_NAME";
    private Context ctx;

    /**
     * Executar no onCreate da Ativity
     * @param ctx
     * @return
     */
    public NotificationAppManager prepareNotifications(Context ctx){
        this.ctx = ctx;
        Intent intent = new Intent("EMAIL_NOVO");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,0,intent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(calendar.SECOND,3);

        AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        return this;
    }

    public void onDestroy(){
        Intent intent = new Intent("EMAIL_NOVO");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,0,intent,0);
        AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void gerarNotificacaoTeste(Context ctx, String titulo, String msg){
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx,EmailContentAct.class);

        intent.putExtra(FRAGMENT_NAME, EmailFrag.class.getSimpleName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,0, intent,0);


        NotificationCompat.Builder builder= new NotificationCompat.Builder(ctx);
        builder.setTicker(titulo);
        builder.setContentTitle(msg);
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_menu_send);
        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ic_menu_send));
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.contentIntent = pendingIntent;
        notification.vibrate = new long[]{150,300,150,300};
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.ic_menu_send,notification);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(ctx,som);
            ringtone.play();
        }catch (Exception ex){

        }
    }
}
