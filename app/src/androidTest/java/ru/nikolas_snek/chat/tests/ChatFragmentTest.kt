package ru.nikolas_snek.chat.tests

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.nikolas_snek.chat.presentation.ChatFragment
import ru.nikolas_snek.chat.screens.ChatFragmentScreen
import ru.nikolas_snek.chat.screens.StreamListScreen
import ru.nikolas_snek.tfsspring2024.MainActivity
import ru.nikolas_snek.tfsspring2024.R
import ru.nikolas_snek.chat.R as RChat

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase() {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)


    private lateinit var mockWebServer: MockWebServer

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
    fun testChatFragmentUI() = run {
        step("Запускаем MainActivity") {
            activityScenarioRule.scenario
        }
        step("Запускаем MainActivity и инициируем запуск ChatFragment") {
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

        step("Проверяем что экран с чатом корректно загружается") {
            ChatFragmentScreen {
                frameLayout {
                    isVisible()
                }
                tvStreamTitle {
                    isVisible()
                    hasText("another channel")
                }
                tvTopicTitle {
                    isVisible()
                    hasText("Topic: myTopic")
                }
                pdChat {
                    isVisible()
                }
                recyclerView {
                    isGone()
                }
                etMessageContent {
                    isVisible()
                }
                btSentMessage {
                    isGone()
                }
            }
        }
    }

    @Test
    fun setReaction() = run {
        step("Запускаем MainActivity") {
            activityScenarioRule.scenario
        }
        step("запускаем ChatFragment") {
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

        step("Ожидаем кока прогрузиться список сообщений ") {
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

        step("Выбираем первое сообщение в списке и осуществляем долгое нажатие на него") {
            ChatFragmentScreen {
                recyclerView {
                    childAt<ChatFragmentScreen.ChatItemType1>(0) {
                        ivUserPortrait {
                            perform { longClick() }
                        }
                    }
                }
            }
            onView(withId(RChat.id.rvEmojis)).check(matches(isDisplayed()))
        }
    }


    @Test
    fun testSendMessage() = run {
        step("Запускаем MainActivity и инициируем запуск ChatFragment") {
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

        step("Введите текст в поле etMessageContent и проверьте видимость кнопок") {
            ChatFragmentScreen {
                etMessageContent.replaceText("Hello World")

                btSentFile.isNotDisplayed()
                btSentMessage.isDisplayed()
            }
        }

        step("Отправьте сообщение и проверьте результаты") {
            ChatFragmentScreen {
                btSentMessage.click()

                etMessageContent.hasText("")
                btSentMessage.isNotDisplayed()
                btSentFile.isDisplayed()
            }
        }
    }

}

