package com.dreamspace.superman.UI.Activity.Person;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Orders.CancelFragment;
import com.dreamspace.superman.UI.Fragment.Orders.CompleteFragment;
import com.dreamspace.superman.UI.Fragment.Orders.NoMeetFragment;
import com.dreamspace.superman.UI.Fragment.Orders.NopaymentFragment;
import com.dreamspace.superman.UI.Fragment.Orders.SubscribeFragment;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

public class OrderListActivity extends AbsActivity {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    private BaseFragment[] mFragments = {new SubscribeFragment(),new NopaymentFragment() ,new NoMeetFragment(),new CompleteFragment(),new CancelFragment()};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initViews() {
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_layout);
//        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_view,R.id.tab_tv);
        mAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        final int color = getResources().getColor(R.color.navi_color);
        final int normalcolor = getResources().getColor(R.color.near_black);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setStartColor(normalcolor);
        mSlidingTabLayout.setFillTheWidth(false);
        mSlidingTabLayout.setViewPager(mViewPager);
        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return color;
            }

            @Override
            public int getSelectedTitleColor(int position) {
                return color;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return normalcolor;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
