package com.rokpetek.breadcrumbapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar;
import com.rokpetek.breadcrumbtoolbar.BreadcrumbToolbar.BreadcrumbToolbarListener;

public class BreadcrumbActivity extends AppCompatActivity implements BreadcrumbToolbarListener {

    private static final String TAG = BreadcrumbActivity.class.getSimpleName();


    // Gui
    private BreadcrumbToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadcrumb);

        // We can't use setSupportActionBar()
        toolbar = (BreadcrumbToolbar) findViewById(R.id.toolbar);
        toolbar.setBreadcrumbToolbarListener(this);
        toolbar.setTitle(R.string.app_name);
        getSupportFragmentManager().addOnBackStackChangedListener(toolbar);

        if (savedInstanceState == null) {
            BreadcrumbFragment.open(this, false, null);
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_breadcrumb, menu);
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
}
