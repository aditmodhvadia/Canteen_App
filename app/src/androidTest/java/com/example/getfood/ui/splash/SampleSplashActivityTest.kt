package com.example.getfood.ui.splash

import android.widget.TextView
import androidx.test.rule.ActivityTestRule
import com.example.getfood.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SampleSplashActivityTest {
    @Rule
    var mActivityTest = ActivityTestRule(SplashActivity::class.java)
    private var splashActivity: SplashActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        splashActivity = mActivityTest.activity
    }

    @Test
    @Throws(Exception::class)
    fun sampleTestLaunch() {
        val tvVersionName = splashActivity!!.findViewById<TextView>(R.id.tvVersionName)!!
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        splashActivity = null
    }
}