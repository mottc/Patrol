package com.mottc.patrol.choose;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RadioGroup;

import com.hyphenate.chat.EMClient;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.login.LoginActivity;
import com.mottc.patrol.manager.ManagerActivity;
import com.mottc.patrol.staff.StaffActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseActivity extends AppCompatActivity {

    private boolean isStaff = true;
    public static ChooseActivity sChooseActivity;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.enter)
    Button mEnter;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果登录成功过，直接进入主页面
        if (EMClient.getInstance().isLoggedInBefore()) {
            if (PatrolApplication.getInstance().getCurrentUserName().startsWith("staff_")) {
                startActivity(new Intent(this, StaffActivity.class));
            } else {
                startActivity(new Intent(this, ManagerActivity.class));
            }
            finish();
            return;
        }


        setContentView(R.layout.activity_choose);
        ButterKnife.bind(this);
        sChooseActivity = this;
        mToolbar.setTitle(R.string.choose_your_job);
        setSupportActionBar(mToolbar);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.staff_radio_button) {
                    isStaff = true;
                } else {
                    isStaff = false;
                }
            }
        });
    }


    @OnClick(R.id.enter)
    public void onViewClicked() {
        if (isStaff) {
            startActivity(new Intent(this, LoginActivity.class).putExtra("isStaff", isStaff));
        } else {
            startActivity(new Intent(this, LoginActivity.class).putExtra("isStaff", isStaff));
        }
    }
}
