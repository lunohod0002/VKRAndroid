package com.example.vkr

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import com.example.vkr.fragments.LoginFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    private lateinit var scenario: FragmentScenario<LoginFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_VKR)
    }

    @Test
    fun testEmptyLoginAndPassword_showsFillFieldsToast() {
        Espresso.onView(ViewMatchers.withId(R.id.loginFragment))
            .perform(ViewActions.click())


        Espresso.onView(ViewMatchers.withText("Не удалось выполнить запрос, ошибка: Заполните логин и пароль"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testEmptyPassword_showsFillFieldsToast() {
        Espresso.onView(ViewMatchers.withId(R.id.login_edit_text_txt))
            .perform(ViewActions.typeText("admin"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.login_btn))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("Не удалось выполнить запрос, ошибка: Заполните логин и пароль"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testEmptyLogin_showsFillFieldsToast() {
        Espresso.onView(ViewMatchers.withId(R.id.password_edit_text_txt))
            .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(R.id.login_btn))
            .perform(ViewActions.click())


        Espresso.onView(ViewMatchers.withText("Не удалось выполнить запрос, ошибка: Заполните логин и пароль"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }



}