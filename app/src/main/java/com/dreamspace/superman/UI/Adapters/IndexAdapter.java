package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Course;

import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class IndexAdapter extends BaseAdapter {
    private List<Course> mCourseList;
    private Context mContext;

    public IndexAdapter(List<Course> mCourseList, Context context) {
        this.mCourseList = mCourseList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.index_list_item, parent, false);
            holder = new viewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.course_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        Course course = mCourseList.get(position);
        holder.mTextView.setText(course.getCourseName());
        return convertView;
    }

    private class viewHolder {
        public TextView mTextView;
    }
}
