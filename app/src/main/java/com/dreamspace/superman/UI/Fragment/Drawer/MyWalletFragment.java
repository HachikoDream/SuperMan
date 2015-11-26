package com.dreamspace.superman.UI.Fragment.Drawer;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.event.MoneyRefreshEvent;
import com.dreamspace.superman.model.api.BalanceRes;
import com.dreamspace.superman.model.api.ErrorRes;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyWalletFragment extends BaseLazyFragment {

    @Bind(R.id.my_balance_tv)
    TextView balanceTv;
    @Override
    protected void onFirstUserVisible() {
       getUserBalance();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }
    private void getUserBalance(){
        toggleShowLoading(true,"正在加载中");
        if(NetUtils.isNetworkConnected(getActivity())){
            ApiManager.getService(getActivity().getApplicationContext()).getUserBalance(new Callback<BalanceRes>() {
                @Override
                public void success(BalanceRes balanceRes, Response response) {
                    toggleShowLoading(false,null);
                    StringBuilder sb=new StringBuilder();
                    sb.append("￥");
                    sb.append((float)balanceRes.getBalance()/(float)100);
                    balanceTv.setText(sb.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowLoading(false,null);
                    ErrorRes res= (ErrorRes) error.getBodyAs(ErrorRes.class);
                    toggleShowError(true, res.getReason(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getUserBalance();
                        }
                    });
                }
            });
        }else{
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserBalance();
                }
            });
        }

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(),R.id.content_container);
    }

    @Override
    protected void initViewsAndEvents() {
        EventBus.getDefault().register(this);
    }
    public void onEvent(MoneyRefreshEvent event){
       getUserBalance();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_balance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
