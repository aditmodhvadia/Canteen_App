package com.example.getfood.ui.splash;

import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import com.example.getfood.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SampleSplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTest = new ActivityTestRule<>(SplashActivity.class);

    private SplashActivity splashActivity = null;

    @Before
    public void setUp() throws Exception {

        splashActivity = mActivityTest.getActivity();

    }

    @Test
    public void sampleTestLaunch() throws Exception {

        TextView tvVersionName = splashActivity.findViewById(R.id.tvVersionName);


        assert tvVersionName != null;
    }

    @After
    public void tearDown() throws Exception {

        splashActivity = null;

    }
}