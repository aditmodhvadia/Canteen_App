package com.example.getfood.ui.loginregister

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.getfood.R
import com.example.getfood.ui.base.BaseActivity
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity
import com.example.getfood.ui.loginregister.loginfragment.LoginFragment
import com.example.getfood.ui.loginregister.registerfragment.RegisterFragment
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogSimple.AlertDialogListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult

class LoginActivity constructor() : BaseActivity(), LoginActivityMvpView {
    var exitCount: Int = 0
    var currTime: Long = 0
    var prevTime: Long = 0
    var presenter: LoginActivityPresenter<LoginActivity>? = null
    override val layoutResId: Int
        get() {
            return R.layout.activity_login
        }

    public override fun initViews() {
        presenter = LoginActivityPresenter()
        presenter!!.onAttach(this)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        val mSectionsPagerAdapter: SectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        val mViewPager: ViewPager = findViewById(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
        presenter!!.bindDynamicLinkEmailVerification(intent)
    }

    override val context: Context
        get() = this

    public override fun onSuccessfulVerificationAndSignIn() {
        AlertUtils.showAlertBox(this@LoginActivity, "", "Email Address Verified", "Continue", object : AlertDialogListener {
            public override fun onButtonClicked() {
                if (presenter!!.isUserLoggedIn) {
                    FirebaseInstanceId.getInstance().instanceId
                            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                                override fun onComplete(task: Task<InstanceIdResult>) {
                                    if (!task.isSuccessful) {
                                        Log.d("##FCM", "getInstanceId failed", task.exception)
                                        return
                                    }
                                    // Get new Instance ID token
                                    val token: String = task.result!!.token
                                    presenter!!.updateToken(token)
                                    // Log and toast
                                    Log.d("##FCM", token)
                                }
                            })
                } else {
                    Log.d("##DebugData", "User not signed in")
                }
            }
        })
    }

    public override fun onFailedVerificationOrSignIn(e: Exception) {
        AlertUtils.showAlertBox(this@LoginActivity, "", e.message, getString(R.string.ok), object : AlertDialogListener {
            public override fun onButtonClicked() {}
        })
    }

    public override fun onTokenUpdatedSuccessfully() {
        startActivity(Intent(this@LoginActivity, FoodMenuDisplayActivity::class.java))
    }

    public override fun setListeners() {}
    override fun onStart() {
        super.onStart()
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    public override fun onBackPressed() {
        exitCount++
        if (exitCount == 1) {
            makeText("Press back once more to exit")
            prevTime = System.currentTimeMillis()
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis()
            if (currTime - prevTime > 2000) {
                makeText("Press back once more to exit")
                prevTime = System.currentTimeMillis()
                exitCount = 1
            } else {
                presenter!!.signOutUser()
                finish()
            }
        }
    }

    fun makeText(msg: String?) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager?) : FragmentPagerAdapter((fm)!!) {
        public override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return LoginFragment()
                1 -> return RegisterFragment()
                else -> return LoginFragment()
            }
        }

        public override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}