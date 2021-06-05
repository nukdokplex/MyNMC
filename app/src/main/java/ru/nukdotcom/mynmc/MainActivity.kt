package ru.nukdotcom.mynmc

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.squareup.picasso.Picasso
import ru.nukdotcom.mynmc.databinding.ActivityMainBinding
import ru.nukdotcom.mynmc.helpers.AuthHandler

class MainActivity : AppCompatActivity() {
    companion object {
        public val REQUEST_LOGIN = 1
        public var authHandler: AuthHandler? = null
        public val adminOnlyMenuItems = setOf<Int>(
            R.id.nav_management_auditories,
            R.id.nav_management_groups,
            R.id.nav_management_current_schedule,
            R.id.nav_management_primary_schedule,
            R.id.nav_management_rings_schedule,
            R.id.nav_management_specializations,
            R.id.nav_management_subjects,
            R.id.nav_management_users
        )
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authHandler = AuthHandler(this as Context)
        authHandler!!.reload()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_posts,
            R.id.nav_news,
            R.id.nav_schedule,
            R.id.nav_management_auditories,
            R.id.nav_management_groups,
            R.id.nav_management_current_schedule,
            R.id.nav_management_primary_schedule,
            R.id.nav_management_rings_schedule,
            R.id.nav_management_specializations,
            R.id.nav_management_subjects,
            R.id.nav_management_users,
            ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (authHandler!!.isAuthenticated()){
            Toast.makeText(this, "Вы вошли по именем " + authHandler!!.name, Toast.LENGTH_LONG).show()
        }

        if (!authHandler!!.isAuthenticated() || !setOf("administrator", "system architect").contains(
                authHandler!!.role!!)){
            val menu = navView.menu
            menu.setGroupVisible(R.id.menu_management, false)
        }

        navView.getHeaderView(0).apply {
            if (authHandler!!.isAuthenticated()){
                this.findViewById<TextView>(R.id.name).setText(authHandler!!.name)
                this.findViewById<TextView>(R.id.email).setText(authHandler!!.email)
                Picasso.get().load(authHandler!!.avatar).into(this.findViewById<ImageView>(R.id.avatar))
            }
            else {
                this.findViewById<TextView>(R.id.name).setText("Гость")
                this.findViewById<TextView>(R.id.email).setText("")
                Picasso.get().load(getString(R.string.default_avatar)).error(R.drawable.default_avatar).into(this.findViewById<ImageView>(R.id.avatar))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (!authHandler!!.isAuthenticated()){
            menuInflater.inflate(R.menu.main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_login){
            val intent = Intent(this, LoginActivity::class.java)

            startActivityForResult(intent, REQUEST_LOGIN)
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_LOGIN){
            if (resultCode == LoginActivity.RESULT_OK){
                ProcessPhoenix.triggerRebirth(this)
            }
            else {
                Toast.makeText(this, "Системе не удалось вас распознать", Toast.LENGTH_LONG).show()
            }
        }
    }
}