package us.grinnell.myweather

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import us.grinnell.myweather.adapter.PubsListAdapter
import us.grinnell.myweather.data.AppDatabase
import us.grinnell.myweather.data.Item


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddItemDialog.ItemHandler {

    lateinit var pubsListAdapter: PubsListAdapter


    private var editIndex: Int = 0
    internal var RouteNumber: Int = 0

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        val KEY_FIRST = "KEY_FIRST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        RouteNumber = intent.getIntExtra("RouteNumber", 0)
        Log.d("LMAOOOO",RouteNumber.toString())

        fab.setOnClickListener { view ->
            showAddItemDialog()
        }

        initNavDrawer()
        initRecyclerView()
    }

    //open a dialog with empty fields to create a new item
    private fun showAddItemDialog() {
        AddItemDialog().show(supportFragmentManager, "TAG_CREATE")
    }

    private fun initNavDrawer() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the toolbar
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handles toolbar item clicks
        when (item.itemId) {
            R.id.action_clear_checked -> {
                pubsListAdapter.clearChecked()
            }
            R.id.action_delete_all -> {
                pubsListAdapter.removeAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        Thread {
            val pubs = AppDatabase.getInstance(this@MainActivity).pubsListDao().findAllPubs()


            pubsListAdapter = PubsListAdapter(this@MainActivity, pubs, RouteNumber)
            runOnUiThread {
                recyclerCities.adapter = pubsListAdapter
            }
        }.start()
    }

    //load values into the dialog fields based on current Item values
    public fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx
        val editItemDialog = AddItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
            "EDITITEMDIALOG")
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_add_city -> {
                AddItemDialog().show(supportFragmentManager, "TAG_CREATE")
            }
            R.id.nav_about -> {
                Toast.makeText(this@MainActivity, getString(R.string.about_message), Toast.LENGTH_LONG).show()
            }
            R.id.qr_code -> {
                var intentStart = Intent()
                intentStart.setClass(this@MainActivity, QRCodeActivity::class.java)
                startActivity(intentStart)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun itemCreated(item: Item) {
        Thread {
            val id = AppDatabase.getInstance(this).pubsListDao().insertPub(item)
            //update the item's id based on the generated id value so that we can access this item later
            item.pubId = id

            runOnUiThread {
                //add the item object to the recycler view
                pubsListAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        val dbThread = Thread {
            //update in the database
            AppDatabase.getInstance(this@MainActivity).pubsListDao().updatePub(item)

            //update in the recycler view
            runOnUiThread { pubsListAdapter.updateItem(item, editIndex) }
        }
        dbThread.start()
    }
}
