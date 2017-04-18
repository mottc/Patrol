package com.mottc.patrol.staff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mottc.patrol.Constant;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.Task;
import com.mottc.patrol.data.source.local.TaskDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.executor_name)
    TextView mExecutorName;
    @BindView(R.id.announcer_name)
    TextView mAnnouncerName;
    @BindView(R.id.time_text)
    TextView mTimeText;
    @BindView(R.id.location_text)
    TextView mLocationText;
    @BindView(R.id.status_text)
    TextView mStatusText;
    @BindView(R.id.done)
    Button mDone;
    private Task mTask;
    private TaskDao mTaskDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        mTaskDao = PatrolApplication.getInstance().getDaoSession().getTaskDao();
        long taskId = this.getIntent().getLongExtra("taskId", -1);
        mTask = mTaskDao.load(taskId);
        initView();
    }

    private void initView() {
        mToolbar.setTitle("任务详情");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mExecutorName.setText(mTask.getExecutor().substring(6));
        mAnnouncerName.setText(mTask.getAnnouncer().substring(8));
        mTimeText.setText(mTask.getTime());
        mLocationText.setText(mTask.getLocation());
        if (mTask.getStatus() == Constant.TASK_STATUS_UNDONE) {
            mStatusText.setText("未完成");
        } else {
            mStatusText.setText("已完成");
            mDone.setClickable(false);
        }
    }

    @OnClick(R.id.done)
    public void onViewClicked() {
        mStatusText.setText("已完成");
        mDone.setClickable(false);
        Task task = new Task(mTask.getId(), mTask.getExecutor(), mTask.getAnnouncer(), mTask.getTime(), mTask.getLocation(), Constant.TASK_STATUS_DONE);
        mTaskDao.update(task);
        TaskFragment.newInstance().updateTaskList();
    }
}
