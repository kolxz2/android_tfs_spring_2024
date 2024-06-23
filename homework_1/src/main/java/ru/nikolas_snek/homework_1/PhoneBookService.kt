package ru.nikolas_snek.homework_1

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.provider.CallLog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

internal class PhoneBookService : Service() {

    private val localBroadcastReceiver by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private val executorService = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        executorService.submit {
            val incomingCall = getLastIncomingCallDetails()
            when (incomingCall) {
                is ContactLogResponse.Success -> {
                    Intent(PhoneActivity.PHONE_BOOK_INTENT_ACTION).apply {
                        putExtra(PhoneActivity.PHONE_BOOK_LAST_CALL, incomingCall.data)
                        localBroadcastReceiver.sendBroadcast(this)
                    }
                }

                ContactLogResponse.Failure -> {
                    Intent(PhoneActivity.PHONE_BOOK_INTENT_ACTION).apply {
                        localBroadcastReceiver.sendBroadcast(this)
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun getLastIncomingCallDetails(): ContactLogResponse<IncomingCall> {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        try {
            val cursor: Cursor? = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
            )

            cursor?.use { phoneCall ->
                val numberIndex = phoneCall.getColumnIndex(CallLog.Calls.NUMBER)
                val dateIndex = phoneCall.getColumnIndex(CallLog.Calls.DATE)
                val durationIndex = phoneCall.getColumnIndex(CallLog.Calls.DURATION)

                if (phoneCall.moveToFirst()) {
                    return ContactLogResponse.Success(
                        createIncomingCall(
                            numberIndex,
                            phoneCall,
                            dateIndex,
                            durationIndex
                        )
                    )
                }
            }
        } catch (e: Exception) {
            return ContactLogResponse.Failure
        }
        return ContactLogResponse.Failure
    }

    private fun createIncomingCall(
        numberIndex: Int,
        phoneCall: Cursor,
        dateIndex: Int,
        durationIndex: Int,
    ): IncomingCall {
        val number = if (numberIndex != -1) phoneCall.getString(numberIndex) else ""
        val date = if (dateIndex != -1) phoneCall.getString(dateIndex) else ""
        val duration = if (durationIndex != -1) phoneCall.getString(durationIndex).toLong() else 0
        return IncomingCall(number, formatDateCall(date), formatTimeDuration(duration))
    }

    private fun formatDateCall(date: String): String {
        val milliseconds = date.toLong()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(milliseconds))
    }

    private fun formatTimeDuration(duration: Long): String {
        val minutes = duration / 60
        val seconds = duration % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}