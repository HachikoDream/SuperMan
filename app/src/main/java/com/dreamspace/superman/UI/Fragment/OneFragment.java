package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {

    private LoadMoreListView moreListView;
    private IndexAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_id);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        moreListView= (LoadMoreListView)view.findViewById(R.id.load_more_lv);
        mAdapter=new IndexAdapter(getTestData(),getActivity());
        moreListView.setAdapter(mAdapter);
        moreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i("onLoad", "on Load more");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Log.i("onLoad", "on load complete");
                        moreListView.setLoading(false);
                    }
                }, 3000);
            }
        });
        return view;
    }
     public List<Course> getTestData(){
         List<Course> mCourses=new ArrayList<>();
         Course course;
         for (int i = 0; i < 10; i++) {
             course=new Course();
             course.setCourseName("技术盲如何在创业初期搞定技术，低成本推出产品"+i);
             mCourses.add(course);
         }
         return mCourses;
     }


}
