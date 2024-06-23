package ru.nikolas_snek.homework_1

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import ru.nikolas_snek.homework_1.databinding.ActivityPhoneBinding

internal class PhoneActivity : AppCompatActivity() {

    private var _binding: ActivityPhoneBinding? = null

    private val requestCameraPermissionLauncher = registerForActivityResult(
        RequestPermission(),
        ::onGotCallLogPermissionResult
    )

    private val binding: ActivityPhoneBinding
        get() = requireNotNull(_binding)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == PHONE_BOOK_INTENT_ACTION) {
                if (intent.hasExtra(PHONE_BOOK_LAST_CALL)) {
                    val number = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(PHONE_BOOK_LAST_CALL, IncomingCall::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(PHONE_BOOK_LAST_CALL)
                    }
                    val newIntent = Intent(context, MainActivity::class.java)
                    newIntent.putExtra(MainActivity.PHONE_BOOK_LAST_CALL, number)
                    setResult(RESULT_OK, newIntent)
                    finish()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.call_history_is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private val localBroadcastReceiver by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        requestCameraPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerBroadcast()
        binding.btStartService.setOnClickListener {
            Intent(this, PhoneBookService::class.java).apply {
                startService(this)
            }
        }
    }

    private fun registerBroadcast() {
        val intentFilter = IntentFilter().apply {
            addAction(PHONE_BOOK_INTENT_ACTION)
        }
        localBroadcastReceiver.registerReceiver(receiver, intentFilter)
    }

    private fun onGotCallLogPermissionResult(granted: Boolean) {
        if (granted) {
            binding.btStartService.isEnabled = true
            binding.tvWarningToTheUser.text = getString(R.string.homework_1_security_message_text)
        } else {
            binding.btStartService.isEnabled = false
            binding.tvWarningToTheUser.text =
                getString(R.string.homework_1_allow_access_to_call_logs)
            Snackbar.make(
                binding.root,
                R.string.homework_1_allow_access_to_call_logs,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(R.string.go_to_settings)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastReceiver.unregisterReceiver(receiver)
        _binding = null
    }

    companion object {
        const val PHONE_BOOK_INTENT_ACTION = "PHONE_BOOK_INTENT_ACTION"
        const val PHONE_BOOK_LAST_CALL = "PHONE_BOOK_NUMBER"
    }
}