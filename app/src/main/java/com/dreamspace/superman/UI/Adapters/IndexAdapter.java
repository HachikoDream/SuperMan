package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class IndexAdapter extends BasisAdapter<Lesson, IndexAdapter.viewHolder> {


    public IndexAdapter(Context context){
        super(context,new ArrayList<Lesson>(),viewHolder.class);
    }
    public IndexAdapter(List<Lesson> mLessonList, Context context) {
        super(context, mLessonList, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Lesson entity) {
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

