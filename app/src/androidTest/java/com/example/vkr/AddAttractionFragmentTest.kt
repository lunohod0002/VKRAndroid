package com.example.vkr.presentation.fragments

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.R
import com.example.myapplication.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddAttractionFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var decorView: View

    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<AddAttractionFragment> {
            decorView = requireActivity().window.decorView
        }
    }

    @Test
    fun saveWithEmptyName_showsToast() {
        onView(withId(R.id.add_attraction_btn))
            .perform(scrollTo(), click())

        onView(withText("Введите название"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun addStationWithEmptyFields_showsToast() {
        onView(withId(R.id.add_attraction_add_cell_btn))
            .perform(scrollTo(), click())

        onView(withText("Заполните все поля станции"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun addStationWithoutDistance_showsToast() {
        onView(withId(R.id.add_attraction_station_name_edit))
            .perform(scrollTo(), typeText("Тестовая"), closeSoftKeyboard())
        onView(withId(R.id.add_attraction_station_branch_edit))
            .perform(scrollTo(), typeText("Красная"), closeSoftKeyboard())

        onView(withId(R.id.add_attraction_add_cell_btn))
            .perform(scrollTo(), click())

        onView(withText("Заполните все поля станции"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }
}