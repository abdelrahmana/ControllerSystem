package com.smartangle.controllersystemapp.common.notification
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.SplashActivity
import com.smartangle.controllersystemapp.common.ContainerActivityForFragment
import com.smartangle.controllersystemapp.common.chat.ChatFragment.Companion.MY_TRIGGER
import com.smartangle.util.LocalGson
import com.smartangle.util.LocalGson.getMessageSenderModel
import com.smartangle.util.NameUtils
import com.smartangle.util.PrefsUtil
import me.leolin.shortcutbadger.ShortcutBadger
class FirebaseNotification : FirebaseMessagingService() {
    private val sharedPrefs: SharedPreferences? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) { //  Timber.e(body.toString());
     //   if (remoteMessage.notification != null) { //      Timber.e("Message Notification Body: " + remoteMessage.getNotification().toString());
          //if (remoteMessage.data.size > 0 && remoteMessage.data.containsKey("type")) { // TODO: 2019-12-05 this should open notifications Activity
              val notificationType =
                  (remoteMessage.data[notificationType]?:"notification").toString()
              //    openNotificationNow(notificationType, remoteMessage)
              convertDataToNotification(remoteMessage, notificationType)
       //   }
            /*   } else { // TODO: 2019-12-05 this creates a new notification and opens the splash
                   handleDeepLink(0, remoteMessage)
               }*/
            //       EventBus.getDefault().post(new DrawerActivity.NotificationEvent(1));
      //  }
    }

  /*  private fun openNotificationNow(notificationType: Int, remoteMessage: RemoteMessage) { // Timber.e("notification type : " + notificationType);
        //  Timber.e("notification type : " + remoteMessage.getData().toString());
        convertDataToNotification(remoteMessage, notificationType)
    }*/

    private fun convertDataToNotification(remoteMessage: RemoteMessage, notificationType: String) {
        var intent: Intent? = null
            if (PrefsUtil.isLoggedIn(applicationContext)) {
            // TODO: 2019-12-05 user is logged in and should open his messages or notifications page
            if (notificationType == notificationChat) {  // this is chatting
                intent = Intent(MY_TRIGGER)
                val messageData = LocalGson.getMessageSenderModel(remoteMessage.data?.toString())
                intent.putExtra(NameUtils.other_info,Gson().toJson(messageData?.message_information?.sender))
                sendBroadcast(intent)
                intent = Intent(this, ContainerActivityForFragment::class.java)
                    .putExtra(ContainerActivityForFragment.isThatForChat,true)
                .putExtra(NameUtils.other_info,Gson().toJson(messageData?.message_information?.sender))
            }
            else {
                // not logged in go to splach
                intent = Intent(this, SplashActivity::class.java)
            }

            } else {
                intent = Intent(this, SplashActivity::class.java)
         }
        /*     if (notificationType == 1) {
            intent.putExtra("direction", "notifications");
        } else if (notificationType == 2) {
            intent.putExtra("direction", "messages");
        }*/
        //intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val currentBadgeNumber = 1 //PrefUtils.getCurrentBadgeNumber(getApplicationContext());
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // if Android Version above 8 then we need to create a chanel in the system.
        makeChanel()
        val builder = NotificationCompat.Builder(this, default_notification_channel_id)
        // int badgeCount = 1;
        ShortcutBadger.applyCount(this, currentBadgeNumber + 1) //for 1.1.4+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //    builder.setSmallIcon(R.drawable.test_nn);
            //   builder.setColor(getResources().getColor(R.color.app_color));
            builder.setSmallIcon(R.drawable.logo) // this should change to notification
            builder.setContentTitle(remoteMessage.notification?.title?:"رسالة جديدة")
            builder.setContentText(remoteMessage.notification?.body?:"")
            builder.setSound(soundUri)
            builder.setAutoCancel(true)
            builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body))
            builder.priority = NotificationCompat.PRIORITY_HIGH
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            //            notificationManagerCompat.notify(/*Notification_base_id + 1*/(int) System.currentTimeMillis(), builder.build());
            notificationManagerCompat.notify( /*Notification_base_id + 1*/System.currentTimeMillis().toInt(), builder.build())
        } else { // notification.setSmallIcon(R.drawable.icon);
            builder.setSmallIcon(R.drawable.logo)
            builder.setContentTitle(remoteMessage.notification?.title?:"رسالة جديدة")
            builder.setContentText(remoteMessage.notification?.body?:"")
            builder.setSound(soundUri)
            builder.setAutoCancel(true)
            builder.setContentIntent(pendingIntent).priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) NotificationManager.IMPORTANCE_HIGH else Notification.PRIORITY_MAX
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body))
            builder.priority = NotificationCompat.PRIORITY_HIGH
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            notificationManagerCompat.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    fun makeChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = channel_name
            val description =channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(default_notification_channel_id, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.setShowBadge(true)
            channel.vibrationPattern = null
            channel.enableVibration(false)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        //    PrefUtils.setFireBaseNewToken(getApplicationContext(), s);
        //   Timber.e(TAG, "onNewToken: " + s);
    } /*  public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }*/
    companion object {
        val channel_name = "H_Group"
        val channel_description = "H_Description"
        val default_notification_channel_id = "105"
        val notificationType = "type_notification"

        val notificationChat = "chat"
    }
}