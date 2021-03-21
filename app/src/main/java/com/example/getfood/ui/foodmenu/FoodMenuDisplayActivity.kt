package com.example.getfood.ui.foodmenu

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.getfood.R
import com.example.getfood.ui.base.BaseActivity
import com.example.getfood.ui.cart.CartActivity
import com.example.getfood.ui.loginregister.LoginActivity
import com.example.getfood.ui.map.MapsActivity
import com.example.getfood.ui.orderlist.OrderListActivity
import com.example.getfood.ui.terms.TermsActivity
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogConfirmation.ConfirmationDialogListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class FoodMenuDisplayActivity : BaseActivity(), FoodMenuDisplayActivityMvpView {
    var coordinatorLayoutParent: CoordinatorLayout? = null
    private var exitCount = 0
    private var currTime: Long = 0
    private var prevTime: Long = 0
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var presenter: FoodMenuDisplayActivityPresenter<FoodMenuDisplayActivity>
    private lateinit var chineseFragment: FoodCategoryFragment
    private lateinit var southIndianFragment: FoodCategoryFragment
    private lateinit var sandwichPizzaFragment: FoodCategoryFragment
    override fun initViews() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        coordinatorLayoutParent = findViewById(R.id.CoordinatorLayoutParent)
        val headerView = navigationView.getHeaderView(0)
        val emailTextView = headerView.findViewById<TextView>(R.id.emailTextView)
        presenter = FoodMenuDisplayActivityPresenter()
        presenter.onAttach(this)
        //        TODO: Move to Mvp
        emailTextView.text = presenter.userEmail


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        /**
         * The [ViewPager] that will host the section contents.
         */
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu)
        } else {
            makeText("Action Bar null")
        }
        findViewById<View>(R.id.floatingActionButton).setOnClickListener {
            presenter.openCart()
            openCart()
        }
        mViewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
        mDrawerLayout = findViewById(R.id.drawer_layout)

//        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener { menuItem -> // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            when (menuItem.itemId) {
                R.id.nav_cart -> {
                    presenter.openCart()
                }
                R.id.nav_order -> {
                    openOrderListActivity()
                }
                R.id.nav_terms -> {
                    openTermsActivity()
                }
                R.id.nav_map -> {
                    openMapActivity()
                }
                R.id.nav_contact -> {
                    openEmailClient()
                }
                R.id.nav_reset_password -> {
                    presenter.sendPasswordResetEmail()
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_help -> {
                    Toast.makeText(this@FoodMenuDisplayActivity, getString(R.string.help_pressed), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@FoodMenuDisplayActivity, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
                }
            }
            mDrawerLayout.closeDrawers()
            true
        }
    }

    override fun setListeners() {}
    override fun onPasswordResetEmailSentSuccessfully() {
        Toast.makeText(this@FoodMenuDisplayActivity, getString(R.string.pwd_reset_sent), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onPasswordResetEmailSentFailed(errMsg: String?) {
        Toast.makeText(this@FoodMenuDisplayActivity, errMsg, Toast.LENGTH_SHORT).show()
    }

    override val layoutResId: Int
        get() = R.layout.activity_food_menu_display

    private fun openTermsActivity() {
        startActivity(Intent(this@FoodMenuDisplayActivity, TermsActivity::class.java))
    }

    private fun openOrderListActivity() {
        startActivity(Intent(this@FoodMenuDisplayActivity, OrderListActivity::class.java))
    }

    private fun openMapActivity() {
        startActivity(Intent(this@FoodMenuDisplayActivity, MapsActivity::class.java))
    }

    private fun openEmailClient() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.primary_email)))
        email.putExtra(Intent.EXTRA_CC, arrayOf(getString(R.string.email_1),
                getString(R.string.email_2)))
        email.putExtra(Intent.EXTRA_SUBJECT, R.string.subject_email)
        email.putExtra(Intent.EXTRA_TEXT, """
     Debug Information: ${Build.MANUFACTURER}
     ${Build.DEVICE}
     ${Build.BRAND}
     ${Build.MODEL}
     API Level: ${Build.VERSION.SDK_INT}
     """.trimIndent())

        //need this to prompts email client only
        email.type = getString(R.string.email_type)
        startActivity(Intent.createChooser(email, "Choose an Email client :"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.menuHelp -> {
                makeText(getString(R.string.help_pressed))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_help, menu)
        return true
    }

    override fun onBackPressed() {
        exitCount++
        if (exitCount == 1) {
            showSnackBar(coordinatorLayoutParent, getString(R.string.press_back_exit))
            prevTime = System.currentTimeMillis()
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis()
            if (currTime - prevTime > 2000) {
                showSnackBar(coordinatorLayoutParent, getString(R.string.press_back_exit))
                prevTime = System.currentTimeMillis()
                exitCount = 1
            } else {
                finishAffinity()
            }
        }
    }

    private fun logout() {
        AlertUtils.showConfirmationDialog(this, getString(R.string.logout), getString(R.string.sure_logout),
                getString(R.string.yes), getString(R.string.no), object : ConfirmationDialogListener {
            override fun onPositiveButtonClicked() {
                presenter!!.signOutUser()
                startActivity(Intent(this@FoodMenuDisplayActivity, LoginActivity::class.java))
                finish()
            }

            override fun onNegativeButtonClicked() {}
        })
    }

    override fun openCart() {
        startActivity(Intent(this, CartActivity::class.java))
    }

    override fun cantOpenCart() {
        makeText(getString(R.string.cart_empty))
    }

    fun showSnackBar(parent: View?, msg: String?) {
        val snackbar = Snackbar
                .make(parent!!, msg!!, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    fun makeText(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    if (chineseFragment == null) {
                        chineseFragment = FoodCategoryFragment()
                        val args = Bundle()
                        args.putString(getString(R.string.cat_type), getString(R.string.chinese))
                        chineseFragment!!.arguments = args
                    }
                    chineseFragment!!
                }
                1 -> {
                    if (southIndianFragment == null) {
                        southIndianFragment = FoodCategoryFragment()
                        val args = Bundle()
                        args.putString(getString(R.string.cat_type), getString(R.string.south_indian))
                        southIndianFragment!!.arguments = args
                    }
                    southIndianFragment!!
                }
                2 -> {
                    if (sandwichPizzaFragment == null) {
                        sandwichPizzaFragment = FoodCategoryFragment()
                        val args = Bundle()
                        args.putString(getString(R.string.cat_type), getString(R.string.pizza_sandwich))
                        sandwichPizzaFragment!!.arguments = args
                    }
                    sandwichPizzaFragment!!
                }
                else -> {
                    if (chineseFragment == null) {
                        chineseFragment = FoodCategoryFragment()
                        val args = Bundle()
                        args.putString(getString(R.string.cat_type), getString(R.string.chinese))
                        chineseFragment!!.arguments = args
                    }
                    chineseFragment!!
                }
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }
}