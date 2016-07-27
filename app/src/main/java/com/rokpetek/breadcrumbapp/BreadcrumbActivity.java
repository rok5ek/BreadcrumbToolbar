package com.rokpetek.breadcrumbapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar;
import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar.BreadcrumbToolbarListener;

public class BreadcrumbActivity extends AppCompatActivity implements BreadcrumbToolbarListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BreadcrumbActivity.class.getSimpleName();


    // Gui
    private BreadcrumbToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            BreadcrumbFragment.open(this, false, null);
        }

        bindViews();
    }

    private void bindViews() {
        // Bind toolbar
        toolbar = (BreadcrumbToolbar) findViewById(R.id.toolbar);
        // We can't use setSupportActionBar()
        toolbar.setBreadcrumbToolbarListener(this);
        toolbar.setTitle(R.string.app_name);
        // Set animated drawer icon to toolbar
        DrawerArrowDrawable drawerArrow = new DrawerArrowDrawable(this);
        drawerArrow.setColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setNavigationIcon(drawerArrow);
        getSupportFragmentManager().addOnBackStackChangedListener(toolbar);

        // Bind drawer
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(this);

        // Bind FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            String fragmentName = getString(R.string.breadcrumb_name, getFragmentStackSize() + 1);
            // Open a new fragment
            BreadcrumbFragment.open(this, true, fragmentName);
            // Show snackbar
            Snackbar.make(view, R.string.new_folder_created, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, (View v) -> {
                        // Handle undo
                        getSupportFragmentManager().popBackStackImmediate();
                    }).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Toolbar callbacks
    @Override
    public void onBreadcrumbToolbarItemPop(int stackSize) {
        // We need remove fragments on every "item pop" callback
        Log.d(TAG, "[toolbar] onBreadcrumbToolbarItemPop stackSize:" + stackSize);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public int getFragmentStackSize() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    @Override
    public String getFragmentName() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (fragment instanceof BreadcrumbFragment) {
            return ((BreadcrumbFragment) fragment).getFragmentName();
        }
        return null;
    }

    // Drawer callbacks
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
