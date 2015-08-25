package com.dreamspace.superman.UI.Activity.Main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Register.LoginActivity;
import com.dreamspace.superman.UI.Adapters.VPFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.CollectionFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.MyWalletFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.OrderListFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.ToBeSuperFragment;
import com.dreamspace.superman.UI.View.XViewPager;

import butterknife.Bind;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout headerLayout;
    BaseLazyFragment fragments[] = {new IndexFragment(), new MyWalletFragment(), new OrderListFragment(), new CollectionFragment(), new ToBeSuperFragment()};
    private TextView mSettings;
    private static final int TITLE = R.string.app_name;
    private int currentPage;
    @Bind(R.id.fragment_container)
    XViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.custom_drawerlayout);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mNavigationView = (NavigationView) findViewById(R.id.id_navigation);
        mNavigationView.setNavigationItemSelectedListener(this);
        mSettings = (TextView) findViewById(R.id.footer_item_settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        headerLayout = (RelativeLayout) findViewById(R.id.header_layout);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (null != fragments) {
            mViewPager.setEnableScroll(false);
            mViewPager.setOffscreenPageLimit(fragments.length);
            mViewPager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_conversation) {
            startActivity(new Intent(MainActivity.this, ConListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFragmentTitle(int id) {
        getSupportActionBar().setTitle(getString(id));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawer(mNavigationView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.nav_index:
                mViewPager.setCurrentItem(0, false);
                setFragmentTitle(R.string.nav_item_index);
                return true;
            case R.id.nav_mybalance:
                mViewPager.setCurrentItem(1, false);
                setFragmentTitle(R.string.nav_item_mybalance);
                return true;
            case R.id.nav_myorder:
                mViewPager.setCurrentItem(2, false);
                setFragmentTitle(R.string.nav_item_myorder);
                return true;
            case R.id.nav_mycollect:
                mViewPager.setCurrentItem(3, false);
                setFragmentTitle(R.string.nav_item_mycollect);
                return true;
            case R.id.nav_tobesuperman:
                mViewPager.setCurrentItem(4, false);
                setFragmentTitle(R.string.nav_item_tobesuperman);
                return true;

        }
        return false;
    }
}
