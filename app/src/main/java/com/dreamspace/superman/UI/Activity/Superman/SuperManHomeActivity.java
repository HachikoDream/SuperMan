package com.dreamspace.superman.UI.Activity.Superman;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperManHomeActivity extends AbsActivity {


    private ListView mListView;
    private String[] mTitles={"我的课程","我的订单","我的账户"};
    private int[] mIds={R.drawable.daren_course,R.drawable.daren_order,R.drawable.daren_account};
    List<Map<String,Object>> mapList=new ArrayList<>();
    private String[] itemName={"title","img"};
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_super_man_home);
    }

    @Override
    protected void initDatas() {
        for (int i=0;i<mTitles.length;i++){
            Map<String,Object> mMap=new HashMap<>();
            mMap.put("title",mTitles[i]);
            mMap.put("img",mIds[i]);
            mapList.add(mMap);
        }
    }

    @Override
    protected void initViews() {
        mListView= (ListView) findViewById(R.id.homepage_list);
        SimpleAdapter mAdapter=new SimpleAdapter(this,mapList,R.layout.list_item_in_superman_homepage,itemName,new int[]{R.id.textview,R.id.imageview});
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_super_man_home, menu);
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
