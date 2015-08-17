package com.dreamspace.superman.UI.Activity.Main;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

public class SearchResultActivity extends AbsActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_search_result);
    }

    @Override
    protected void prepareDatas() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("INFO",query);
//            doMySearch(query);
        }
    }

    @Override
    protected void initViews() {

    }

}
