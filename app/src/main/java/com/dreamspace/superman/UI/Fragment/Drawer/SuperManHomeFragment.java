package com.dreamspace.superman.UI.Fragment.Drawer;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Superman.MyAccountActivity;
import com.dreamspace.superman.UI.Activity.Superman.MyCourseListActivity;
import com.dreamspace.superman.UI.Activity.Superman.OrderListActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

public class SuperManHomeFragment extends BaseLazyFragment {


    @Bind(R.id.homepage_list)
    ListView mListView;
    @Bind(R.id.user_avater_iv)
    CircleImageView userIv;
    @Bind(R.id.username_tv)
    TextView userNameTv;
    private String[] mTitles={"我的课程","我的订单","我的账户"};
    private int[] mIds={R.drawable.daren_course,R.drawable.daren_order,R.drawable.daren_account};
    List<Map<String,Object>> mapList=new ArrayList<>();
    private String[] itemName={"title","img"};


    protected void prepareDatas() {
        for (int i=0;i<mTitles.length;i++){
            Map<String,Object> mMap=new HashMap<>();
            mMap.put("title",mTitles[i]);
            mMap.put("img",mIds[i]);
            mapList.add(mMap);
        }
    }


    @Override
    protected void onFirstUserVisible() {
        String avater_url=PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.AVATAR);
        String user_name=PreferenceUtils.getString(getActivity().getApplicationContext(),PreferenceUtils.Key.REALNAME);
        Tools.showImageWithGlide(getActivity(),userIv, avater_url);
        userNameTv.setText(user_name);
    }

    @Override
    protected void onUserVisible() {
        String avater_url=PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.AVATAR);
        String user_name=PreferenceUtils.getString(getActivity().getApplicationContext(),PreferenceUtils.Key.REALNAME);
        Tools.showImageWithGlide(getActivity(),userIv, avater_url);
        userNameTv.setText(user_name);
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        prepareDatas();
        SimpleAdapter mAdapter=new SimpleAdapter(getActivity(),mapList,R.layout.list_item_in_superman_homepage,itemName,new int[]{R.id.textview,R.id.imageview});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        readyGo(MyCourseListActivity.class);
                        break;
                    case 1:
                        readyGo(OrderListActivity.class);
                        break;
                    case 2:
                        readyGo(MyAccountActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_super_man_home;
    }
}
