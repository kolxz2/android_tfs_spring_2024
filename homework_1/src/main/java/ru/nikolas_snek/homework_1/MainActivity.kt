package ru.nikolas_snek.homework_1

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.nikolas_snek.homework_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: IncomingCall? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(
                            PhoneActivity.PHONE_BOOK_LAST_CALL,
                            IncomingCall::class.java
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getParcelableExtra(PhoneActivity.PHONE_BOOK_LAST_CALL)
                    }
                data ?: return@registerForActivityResult
                binding.tvWelcomeMessage.text = String.format(
                    getString(R.string.homework_1_last_call_info),
                    data.number,
                    data.callDate,
                    data.duration
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btGoToNextScreen.setOnClickListener {
            val intent = Intent(this, PhoneActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val PHONE_BOOK_LAST_CALL = "PHONE_BOOK_NUMBER"
    }
}