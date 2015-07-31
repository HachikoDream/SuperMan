package com.dreamspace.superman.UI.Main;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.IndexFragment;
import com.dreamspace.superman.UI.View.AbsActivity;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private static  final int TITLE=R.string.app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.custom_drawerlayout);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
        mDrawerLayout=(DrawerLayout)findViewById(R.id.dl_left);
        mNavigationView=(NavigationView)findViewById(R.id.id_navigation);
        mNavigationView.setNavigationItemSelectedListener(this);
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
        IndexFragment indexFragment=new IndexFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,indexFragment).commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_settings) {
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
        switch (menuItem.getItemId()){
            case R.id.nav_setclass:
                setFragmentTitle(R.string.nav_item_setclass);
                return true;
            case R.id.nav_mybalance:
                setFragmentTitle(R.string.nav_item_mybalance);
                return true;
            case R.id.nav_myorder:
                setFragmentTitle(R.string.nav_item_myorder);
                return true;
            case R.id.nav_mycollect:
                setFragmentTitle(R.string.nav_item_mycollect);
                return true;
            case R.id.nav_tobesuperman:
                setFragmentTitle(R.string.nav_item_tobesuperman);
                return true;
        }
        return false;
    }
}
