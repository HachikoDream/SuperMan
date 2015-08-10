package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class IndexAdapter extends BasisAdapter<Course, IndexAdapter.viewHolder> {


    public IndexAdapter(Context context){
        super(context,new ArrayList<Course>(),viewHolder.class);
    }
    public IndexAdapter(List<Course> mCourseList, Context context) {
        super(context, mCourseList, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Course entity) {
        holder.mTextView.setText(entity.getCourseName());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.mTextView = (TextView) convertView.findViewById(R.id.course_name_tv);
    }

    @Override
    public int getItemLayout() {
        return R.layout.index_list_item;
    }

    public static class viewHolder {

        public TextView mTextView;
    }

}

