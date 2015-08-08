package com.dreamspace.superman.UI.Activity.Person;

import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

public class OrderDetailActivity extends AbsActivity {

   private final int TITLE=R.string.title_activity_order_detail;
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initDatas() {
       getSupportActionBar().setTitle(TITLE);
    }

    @Override
    protected void initViews() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
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
