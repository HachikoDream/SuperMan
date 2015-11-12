package com.dreamspace.superman.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.dreamspace.superman.UI.SmApplication;
import com.dreamspace.superman.event.DbChangeEvent;
import com.dreamspace.superman.event.ImTypeMessageEvent;
import com.ds.greendao.Conversation;
import com.ds.greendao.ConversationDao;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

/**
 * Created by Wells on 2015/11/12.
 */
public class ConverListLoader extends AsyncTaskLoader<List<Conversation>> {

    private Context context;
    private List<Conversation> mConversations;
    public ConverListLoader(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public List<Conversation> loadInBackground() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        return DbRelated.queryAll(context);
    }

    @Override
    public void deliverResult(List<Conversation> data) {
        mConversations=data;
        if(isStarted()){
            super.deliverResult(data);
        }

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mConversations!=null){
            deliverResult(mConversations);
        }
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if(takeContentChanged()){
            forceLoad();
        }else if(mConversations==null){
            forceLoad();
        }

    }

    @Override
    protected void onStopLoading() {
      cancelLoad();
    }

    @Override
    protected void onReset() {
      if (mConversations!=null){
          mConversations=null;
      }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCanceled(List<Conversation> data) {
        super.onCanceled(data);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    public void onEvent(DbChangeEvent event) {
        Log.i("DBCHANGE","db change");
        onContentChanged();
    }
}
