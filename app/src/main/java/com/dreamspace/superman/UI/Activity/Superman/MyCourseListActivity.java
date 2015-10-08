package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.BaseListAct;
import com.dreamspace.superman.UI.Adapters.MyCourseAdapter;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class MyCourseListActivity extends BaseListAct<Lesson> {


    public MyCourseListActivity() {
        super(MyCourseAdapter.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_course_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_course) {
            Intent intent=new Intent(this,AddCourseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPullUp() {

    }

    @Override
    public void onPullDown() {

    }

    @Override
    public void getInitData() {
     refreshDate(getTestData(),BaseListAct.LOAD);
    }
    public List<Lesson> getTestData() {
        List<Lesson> mLessons = new ArrayList<>();
        Lesson lesson;
        for (int i = 0; i < 10; i++) {
            lesson = new Lesson();
            lesson.setCourseName("创业初期搞定技术低成本推出产品" + i);
            mLessons.add(lesson);
        }
        return mLessons;
    }
}
