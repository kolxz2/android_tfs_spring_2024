package ru.nikolas_snek.chat.tests

import android.os.Bundle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.nikolas_snek.chat.presentation.ChatFragment
import ru.nikolas_snek.chat.screens.ChatFragmentScreen
import ru.nikolas_snek.tfsspring2024.MainActivity
import ru.nikolas_snek.tfsspring2024.R


@RunWith(AndroidJUnit4::class)
class SampleUITest : TestCase(Kaspresso.Builder.default()) {

    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun sampleUITestWithMockWebServer() = run {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"message\": \"Hello, world!\"}")
        mockWebServer.enqueue(mockResponse)

        step("Launch MainActivity and navigate to ChatFragment") {
            val fragmentArgs = Bundle().apply {
                putString("STREAM_TITLE", "another channel")
                putString("TOPIC_TITLE", "myTopic")
            }
            activityScenarioRule.scenario.onActivity { activity ->
                val fragment = ChatFragment().apply {
                    arguments = fragmentArgs
                }
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commitNow()
            }
        }

        step("Wait for data to load and verify UI updates") {
            flakySafely(timeoutMs = 10000) {
                ChatFragmentScreen {
                    pdChat {
                        isGone()
                    }
                    recyclerView {
                        isVisible()
                    }
                }
            }
        }
    }
}
