package com.dreamspace.superman.UI.Fragment.Orders.Person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.UI.Activity.BaseListAct;
import com.dreamspace.superman.UI.Activity.Person.OrderDetailActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyOrderFragment;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.event.OrderChangeEvent;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.Order;
import com.dreamspace.superman.model.OrderClassify;
import com.dreamspace.superman.model.api.OrderlistRes;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class SubscribeFragment extends BaseLazyOrderFragment<Order> {
    private OrderClassify selfCatalog;
    private boolean onFirst = false;
    private boolean onPage = false;
    private int page = 1;
    private static final int DEFAULT_PAGE = 1;
    private static final int REQUEST_CODE = 345;

    public SubscribeFragment() {

    }


    @Override
    public void onPullUp() {
        getOrderListByPage(++page, new OnRefreshListener<Order>() {
            @Override
            public void onFinish(List<Order> mEntities) {
                onPullUpFinished();
                if (mEntities.isEmpty()) {
                    showToast("没有更多的数据");
                } else {
                    refreshDate(mEntities, BaseListAct.ADD);
                }
            }

            @Override
            public void onError() {
                onPullUpFinished();
            }
        });
    }

    @Override
    public void onPullDown() {
        page = 1;
        getOrderListByPage(page, new OnRefreshListener<Order>() {
            @Override
            public void onFinish(List<Order> mEntities) {
                onPullDownFinished();
                refreshDate(mEntities, BaseListAct.LOAD);
            }

            @Override
            public void onError() {
                onPullDownFinished();
            }
        });

    }

    @Override
    public void getInitData() {
        EventBus.getDefault().register(this);
        onFirst = true;
        if (selfCatalog != null) {
            loadingDataWhenInit();
        }
    }

    @Override
    protected void onItemPicked(Order item, int position) {
        Bundle b = new Bundle();
        b.putInt(OrderDetailActivity.ORDER_ID, item.getId());
        b.putInt(OrderDetailActivity.STATE, selfCatalog.getState());
        b.putString(OrderDetailActivity.COMMON_PRICE, CommonUtils.getPriceWithInfo(item.getLess_price()));
        readyGoForResult(OrderDetailActivity.class, REQUEST_CODE, b);
    }

    public void onPageSelected(int position, OrderClassify catalog) {
        selfCatalog = catalog;
        if (onFirst && !onPage) {
            onPage = true;
            loadingDataWhenInit();
        }
    }

    private void loadingDataWhenInit() {
        page=1;
        toggleShowLoading(true, null);
        getOrderListByPage(DEFAULT_PAGE, new OnRefreshListener<Order>() {
            @Override
            public void onFinish(List<Order> mEntities) {
                toggleShowLoading(false, null);
                if (mEntities.isEmpty()) {
                    toggleShowError(true, "暂时还没有相关订单,点击图标重新获取", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingDataWhenInit();
                        }
                    });
                } else {
                    refreshDate(mEntities, BaseListAct.LOAD);
                }
            }

            @Override
            public void onError() {
                toggleShowError(true, "暂时不能获取到相关信息,请稍后重试", null);

            }
        });


    }

    private void getOrderListByPage(int page, final OnRefreshListener<Order> listener) {
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).getOrderListById(selfCatalog.getState(), page, new Callback<OrderlistRes>() {
                @Override
                public void success(OrderlistRes order, Response response) {
                    if (order != null) {
                        listener.onFinish(order.getOrders());
                    } else {
                        listener.onError();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showNetWorkError();
                    listener.onError();
                }
            });
        } else {
            showNetWorkError();
            listener.onError();
        }
    }

    public void onEvent(OrderChangeEvent event){
        loadingDataWhenInit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
