package com.example.aoppart3chapter01

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService:FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)

        createNotificationChannel()

        val type = msg.data["type"]?.let {
            NotificationType.valueOf(it)
        }


        val title = msg.data["title"]
        val message = msg.data["message"]

        type ?: return




        NotificationManagerCompat
            .from(this)
            .notify(1,createNotification(type,title,message))


    }
    companion object{
        const val CHANNEL_NAME = "Emoji Party"
        const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        const val CHANNEL_ID = "Channel Id"
    }


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: NotificationType?, title:String?, message:String?): Notification {

        val intent = Intent(this,MainActivity::class.java)

        intent.apply{
            putExtra("notificationType","${type?.title}타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }


        val pendingIntent = type?.id?.let {
            PendingIntent.getActivity(this, it,intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when(type){
            NotificationType.NORMAL->Unit

            NotificationType.EXPANDABLE->{
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                              "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻" +
                                 "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻" +
                                 "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻" +
                                 "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻" +
                                 "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻" +
                                 "♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻♻"
                    )
                )
            }

            NotificationType.CUSTOM->{
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title,title)
                            setTextViewText(R.id.message,message)
                        }
                    )
            }

        }

        return notificationBuilder.build()

    }






}