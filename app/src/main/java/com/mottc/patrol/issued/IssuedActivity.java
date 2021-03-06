package com.mottc.patrol.issued;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.PatrolDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssuedActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.executor)
    TextView mExecutor;
    @BindView(R.id.time_show)
    TextView mTimeShow;
    @BindView(R.id.time_button)
    Button mTimeButton;
    @BindView(R.id.location)
    EditText mLocation;
    @BindView(R.id.datePicker)
    DatePicker mDatePicker;
    @BindView(R.id.done)
    Button mDone;

    private String username;
    private PatrolDate mPatrolDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued);
        ButterKnife.bind(this);
        username = this.getIntent().getStringExtra("username");

        mToolbar.setTitle("分配任务");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mExecutor.setText(username.substring(6));

    }

    @OnClick({R.id.time_button, R.id.done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_button:
                mDatePicker.setVisibility(View.VISIBLE);
                mTimeButton.setVisibility(View.INVISIBLE);
                mDatePicker.init(2017, 5, 6, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mPatrolDate = new PatrolDate(year, monthOfYear + 1, dayOfMonth);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                                0, 0);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                        mTimeShow.setText(format.format(calendar.getTime()));
                        mDatePicker.setVisibility(View.GONE);
                        mTimeButton.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.done:
                if (mTimeShow.getText().toString().isEmpty()) {
                    Toast.makeText(this, "还未选择时间！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mLocation.getText().toString().isEmpty()) {
                    Toast.makeText(this, "还未输入地点！", Toast.LENGTH_SHORT).show();
                    return;
                }


                mLocation.clearFocus();

                String content = "{\"time\": " + mPatrolDate.toString() + "," + "\"location\":" + mLocation.getText().toString() + "}";
                EMMessage message = EMMessage.createTxtSendMessage(content, username);
                EMClient.getInstance().chatManager().sendMessage(message);

                mDone.setClickable(false);
                Toast.makeText(this, "已发送", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
