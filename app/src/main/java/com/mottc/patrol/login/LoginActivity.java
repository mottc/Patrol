package com.mottc.patrol.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mottc.patrol.R;
import com.mottc.patrol.choose.ChooseActivity;
import com.mottc.patrol.manager.ManagerActivity;
import com.mottc.patrol.staff.StaffActivity;

public class LoginActivity extends AppCompatActivity {

    private boolean isStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isStaff = this.getIntent().getBooleanExtra("isStaff", true);
        if (isStaff) {
            startActivity(new Intent(this, StaffActivity.class));
        } else {
            startActivity(new Intent(this, ManagerActivity.class));
        }
        ChooseActivity.sChooseActivity.finish();
        finish();
    }
}
