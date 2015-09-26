package com.dreamspace.superman.UI.Fragment;


import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCommentFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Comment;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StudentCommentListFragment extends BaseLazyCommentFragment<Comment> {


    @Override
    protected void getInitData() {

    }

    @Override
    protected void onItemPicked(Comment item, int position) {

    }


    @Override
    protected void onPullUp() {

    }

    @Override
    protected void onPullDown() {

    }
}
