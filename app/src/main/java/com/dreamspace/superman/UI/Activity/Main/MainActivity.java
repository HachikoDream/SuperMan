package com.dreamspace.superman.UI.Activity.Main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
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
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Drawer.ChooseClassifyFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.MyWalletFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.OrderListFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.ToBeSuperFragment;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private IndexFragment mIndexFragment;
    private ChooseClassifyFragment mChooseClassifyFragment;
    private MyWalletFragment myWalletFragment;
    private OrderListFragment mOrderListFragment;
    private ToBeSuperFragment mToBeSuperFragment;
    private RelativeLayout headerLayout;
    private TextView mSettings;
    private static  final int TITLE=R.string.app_name;
    private int currentPage;

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
        mDrawerLayout=(DrawerLayout)findViewById(R.id.dl_left);
        mNavigationView=(NavigationView)findViewById(R.id.id_navigation);
        mNavigationView.setNavigationItemSelectedListener(this);
        mSettings=(TextView)findViewById(R.id.footer_item_settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });
        headerLayout=(RelativeLayout)findViewById(R.id.header_layout);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mIndexFragment=new IndexFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mIndexFragment).commit();
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
            startActivity(new Intent(MainActivity.this,ConListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setFragmentTitle(int id){
        getSupportActionBar().setTitle(getString(id));
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawer(mNavigationView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()){
            case R.id.nav_setclass:
                setCurrentPage(R.id.nav_setclass);
                if(mChooseClassifyFragment==null){
                    mChooseClassifyFragment=new ChooseClassifyFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_container,mChooseClassifyFragment).commit();
                setFragmentTitle(R.string.nav_item_setclass);
                return true;
            case R.id.nav_mybalance:
                setCurrentPage(R.id.nav_mybalance);
                if(myWalletFragment==null){
                    myWalletFragment=new MyWalletFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_container, myWalletFragment).commit();
                setFragmentTitle(R.string.nav_item_mybalance);
                return true;
            case R.id.nav_myorder:
                if(getCurrentPage()!=R.id.nav_myorder){
                    setCurrentPage(R.id.nav_myorder);
                    mOrderListFragment=new OrderListFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, mOrderListFragment).commit();
                    setFragmentTitle(R.string.nav_item_myorder);
                }
                return true;
            case R.id.nav_mycollect:
                setCurrentPage(R.id.nav_mycollect);
                setFragmentTitle(R.string.nav_item_mycollect);
                return true;
            case R.id.nav_tobesuperman:
                setCurrentPage(R.id.nav_tobesuperman);
                if(mToBeSuperFragment==null){
                    mToBeSuperFragment=new ToBeSuperFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_container, mToBeSuperFragment).commit();
                setFragmentTitle(R.string.nav_item_tobesuperman);
                return true;
            case R.id.nav_index:
                if(getCurrentPage()!=R.id.nav_index){
                    setCurrentPage(R.id.nav_index);
//                    if(mIndexFragment==null){
                        mIndexFragment=new IndexFragment();
//                    }
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, mIndexFragment).addToBackStack(null).commit();
                    setFragmentTitle(R.string.nav_item_index);
                }
                return true;
        }
        return false;
    }
}
