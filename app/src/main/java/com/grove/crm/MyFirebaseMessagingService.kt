
package com.grove.crm

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grove.crm.utils.AccountManager
import com.grove.crm.utils.Logger

class MyFirebaseMessagingService : FirebaseMessagingService() {
  private var broadcaster: LocalBroadcastManager? = null
  private val processLater = false

  override fun onCreate() {
    broadcaster = LocalBroadcastManager.getInstance(this)
  }

  override fun onNewToken(token: String) {
    Logger.d("Refreshed token: $token")
    AccountManager.fcmToken = token
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)

    Logger.d("From: ${remoteMessage.from}")


    if (/* Check if data needs to be processed by long running job */ processLater) {
      //scheduleJob()
      Logger.d("executing schedule job")
    } else {
      // Handle message within 10 seconds
      handleNow(remoteMessage)
    }
  }

  private fun handleNow(remoteMessage: RemoteMessage) {
    val handler = Handler(Looper.getMainLooper())

    handler.post {

      remoteMessage.notification?.let {
        val intent = Intent("MyData")
        intent.putExtra("message", remoteMessage.data["text"])
        broadcaster?.sendBroadcast(intent)
      }
    }
  }
}