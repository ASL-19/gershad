package com.gershad.gershad

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import com.gershad.gershad.about.AboutActivity
import com.gershad.gershad.dependency.AndroidInternalsModule
import com.gershad.gershad.dependency.GershadPreferences
import com.gershad.gershad.dependency.Module
import com.gershad.gershad.faq.FaqActivity
import com.gershad.gershad.map.MapActivity
import com.gershad.gershad.onboarding.OnboardingActivity
import com.gershad.gershad.reports.ReportsActivity
import com.gershad.gershad.savedlocations.SavedLocationsActivity
import com.gershad.gershad.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import space.traversal.kapsule.Injects
import space.traversal.kapsule.inject
import space.traversal.kapsule.required
import io.github.inflationx.viewpump.ViewPumpContextWrapper
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseNavigationActivity(private val layoutId: Int) : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Injects<Module> {

    private val gershadSettings: GershadPreferences by required { preferences }

    /*
     * Lifecycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(BaseApplication.module(this))

        if(BuildConfig.BUILD_TYPE.equals("release")) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        setContentView(layoutId)

        if (!gershadSettings.rawPreferences.getBoolean(AndroidInternalsModule.ONBOARDING_COMPLETE, false)) {
            startActivity(Intent(OnboardingActivity.newIntent(this)))
            finish()
            return
        }

        setupActionBar(R.id.toolbar) {
            title = ""
        }

        initializeNavigationDrawer()
    }

    private fun initializeNavigationDrawer() {
        if (nav_view != null) {
            nav_view.setNavigationItemSelectedListener(this)

            nav_view.menu.findItem(R.id.nav_uninstall).isVisible = gershadSettings.uninstall

            nav_view.menu.findItem(R.id.nav_version).title = getString(R.string.version) + " " + BuildConfig.VERSION_NAME

            val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open)
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar!!.show()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(GershadContextWrapper.wrap(newBase)))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (!item.isChecked) {
            when (item.itemId) {
                R.id.nav_main_map -> {
                    startActivity(MapActivity.newIntent(this))
                }
                R.id.nav_reports -> {
                    startActivity(ReportsActivity.newIntent(this))
                }
                R.id.nav_saved_locations -> {
                    startActivity(SavedLocationsActivity.newIntent(this))
                }
                R.id.nav_feedback -> {
                    try {
                        var intent = createFeedbackEmailIntent()
                        startActivity(intent)
                    } catch (ex: Exception) {
                        Toast.makeText(this, R.string.no_email, Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.nav_about -> {
                    startActivity(AboutActivity.newIntent(this))
                }
                R.id.nav_settings -> {
                    startActivity(SettingsActivity.newIntent(this))
                }
                R.id.nav_help -> {
                    startActivity(FaqActivity.newIntent(this))
                }
                R.id.nav_uninstall -> {
                    val intent = Intent(Intent.ACTION_DELETE)
                    intent.data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    startActivity(intent)
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun createFeedbackEmailIntent(): Intent {
        // From https://stackoverflow.com/questions/69796290/how-to-include-email-recipient-information-in-intents-on-android-12
        val uri = Uri.parse("mailto:")

        return Intent(Intent.ACTION_SENDTO)
            .setData(uri)
            .putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("feedback@gershad.com") //Ive also tried without arrayOf, no difference.
            )
            .putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.gershad_feedback)
            )
            .putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.gershad_version) + BuildConfig.VERSION_NAME
            ).apply {
                selector = Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"))
            }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val NAVIGATION_MAP = 0
        const val NAVIGATION_REPORTS = 1
        const val NAVIGATION_SAVED = 2
    }
}
