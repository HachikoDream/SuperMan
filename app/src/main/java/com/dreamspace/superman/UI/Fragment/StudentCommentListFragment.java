package com.dreamspace.superman.UI.Fragment;


import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Comment;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

public class StudentCommentListFragment extends BaseFragment {

    private LoadMoreListView moreListView;
    private CommentAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static String TAG="学员评论";
    public StudentCommentListFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_student_comment_list;
    }

    @Override
    public void initViews(View view) {
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
        mAdapter=new CommentAdapter(getActivity(),getTestData());
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
    }

    @Override
    public void initDatas() {
    }

    public List<Comment> getTestData(){
        List<Comment> mComments=new ArrayList<>();
        Course course;
        for (int i = 0; i < 10; i++) {
            Comment comment=new Comment();
            comment.setComment("我非常喜欢这个人的讲课风格，花很少的价格学到了很多的东西，同时还交到了朋友");
            mComments.add(comment);
        }
        return mComments;
    }


}
