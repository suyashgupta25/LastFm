package de.appsfactory.lastfm.ui.home.myalbumscreen

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.ui.home.HomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MyAlbumsFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<HomeActivity> = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    @Throws(InterruptedException::class)
    fun testMyAlbumsLoad() {
        Thread.sleep(500L)
        onView(withId(R.id.rv_my_albums)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldBeAbleToOpenDetails() {
        Thread.sleep(500L)
        onView(withId(R.id.rv_my_albums)).check(matches(isDisplayed()))
        val actionOnItemAtPosition = RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        onView(withId(R.id.rv_my_albums)).perform(actionOnItemAtPosition)
        Thread.sleep(800L)
        onView(withId(R.id.tv_album_details_artist)).check(matches(isDisplayed()))
    }
}