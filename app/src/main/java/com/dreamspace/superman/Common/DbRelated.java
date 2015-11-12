package com.dreamspace.superman.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dreamspace.superman.UI.SmApplication;
import com.ds.greendao.Conversation;
import com.ds.greendao.ConversationDao;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by Wells on 2015/11/12.
 */
public class DbRelated {
    public static ConversationDao getConversationDao(Context context) {
        return ((SmApplication) context.getApplicationContext()).getDaoSession().getConversationDao();
    }

    public static SQLiteDatabase getDb(Context context) {
        return ((SmApplication) context.getApplicationContext()).getDb();
    }

    public static Conversation findConById(Context context, long memberId) {
        Conversation result = getConversationDao(context).queryBuilder()
                .where(ConversationDao.Properties.MemberId.eq(memberId))
                .unique();
        return result;
    }

    public static void updateCon(Context context, Conversation conversation) {
        getConversationDao(context).update(conversation);
    }

    public static List<Conversation> queryAll(Context context) {
        Query query = getConversationDao(context).queryBuilder()
                .orderDesc(ConversationDao.Properties.ChatTime)
                .build();
        List<Conversation> result = query.list();
        return result;
    }
    public static void insertCon(Context context,Conversation conversation){
        getConversationDao(context).insert(conversation);
    }
    public static void clearAll(Context context){
        getConversationDao(context).deleteAll();
    }
}
