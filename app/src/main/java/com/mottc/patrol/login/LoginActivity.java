package com.mottc.patrol.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.choose.ChooseActivity;
import com.mottc.patrol.data.source.local.DaoMaster;
import com.mottc.patrol.manager.ManagerActivity;
import com.mottc.patrol.staff.StaffActivity;
import com.mottc.patrol.utils.DisplayUtils;

import org.greenrobot.greendao.database.Database;

import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;

public class LoginActivity extends AppCompatActivity {

    private boolean isStaff;
    private boolean progressShow;

    private String loginUserName;
    private String loginPassword;
    private String RegisterUserName;
    private String RegisterPassword;
    private String RegisterPasswordRep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isStaff = this.getIntent().getBooleanExtra("isStaff", true);


        autoLogin();


        //登陆
        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.loginView);
        ((DefaultLoginView) login.getLoginView()).setListener(new DefaultLoginView.DefaultLoginViewListener() {
            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {


                //Handle login
                loginUserName = loginUser.getEditText().getText().toString();
                if (loginUserName.isEmpty()) {
                    loginUser.setError("User name can't be empty");
                    return;
                }
                loginUser.setError("");

                loginPassword = loginPass.getEditText().getText().toString();
                if (loginPassword.isEmpty()) {
                    loginPass.setError("Password can't be empty");
                    return;
                }
                loginPass.setError("");

                progressShow = true;
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressShow = false;
                    }
                });
                pd.setMessage(getString(R.string.is_landing));
                pd.show();

                if (isStaff) {
                    loginUserName = "staff_" + loginUserName;
                } else {
                    loginUserName = "manager_" + loginUserName;
                }
                // reset current loginUserName name before login
                PatrolApplication.getInstance().setCurrentUserName(loginUserName);
                // close it before login to make sure DemoDB not overlap

                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "task.db");
                Database db = helper.getWritableDb();
                helper.close();
                db.close();

                // 调用sdk登陆方法登陆聊天服务器
                EMClient.getInstance().login(loginUserName, loginPassword, new EMCallBack() {

                    @Override
                    public void onSuccess() {

                        if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                            pd.dismiss();
                        }

                        // 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        if (isStaff) {
                            startActivity(new Intent(LoginActivity.this, StaffActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                        }
                        ChooseActivity.sChooseActivity.finish();
                        finish();

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(final int code, final String message) {
                        if (!progressShow) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.login_failed) + message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });


        //注册
        ((DefaultRegisterView) login.getRegisterView()).setListener(new DefaultRegisterView.DefaultRegisterViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                //Handle register
                RegisterUserName = registerUser.getEditText().getText().toString();
                if (RegisterUserName.isEmpty()) {
                    registerUser.setError("User name can't be empty");
                    return;
                }
                registerUser.setError("");

                for(int i=0;i<RegisterUserName.length();i++) {
                    char c = RegisterUserName.charAt(i);
                    if (Character.isUpperCase(c)) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                RegisterPassword = registerPass.getEditText().getText().toString();
                if (RegisterPassword.isEmpty()) {
                    registerPass.setError("Password can't be empty");
                    return;
                }
                registerPass.setError("");

                RegisterPasswordRep = registerPassRep.getEditText().getText().toString();
                if (!RegisterPassword.equals(RegisterPasswordRep)) {
                    registerPassRep.setError("Passwords are different");
                    return;
                }
                registerPassRep.setError("");

                if (!TextUtils.isEmpty(RegisterUserName) && !TextUtils.isEmpty(RegisterPassword)) {
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage(getResources().getString(R.string.is_registering));
                    pd.show();

                    if (isStaff) {
                        RegisterUserName = "staff_" + RegisterUserName;
                    } else {
                        RegisterUserName = "manager_" + RegisterUserName;
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                // 调用sdk注册方法
                                EMClient.getInstance().createAccount(RegisterUserName, RegisterPassword);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!LoginActivity.this.isFinishing())
                                            pd.dismiss();
                                        // 保存用户名
                                        PatrolApplication.getInstance().setCurrentUserName(RegisterUserName);
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registered_successfully), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("isStaff", isStaff));
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!LoginActivity.this.isFinishing())
                                            pd.dismiss();
                                        int errorCode = e.getErrorCode();
                                        if (errorCode == EMError.NETWORK_ERROR) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_already_exists), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                        } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }).start();

                }

            }
        });

    }

    private void autoLogin() {
        //如果登录成功过，直接进入主页面
        if (EMClient.getInstance().isLoggedInBefore()) {
            if (isStaff) {
                startActivity(new Intent(this, StaffActivity.class));
            } else {
                startActivity(new Intent(this, ManagerActivity.class));
            }
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        /*点击非键盘区，键盘落下*/
        DisplayUtils.hideInputWhenTouchOtherView(this, event, null);
        return super.dispatchTouchEvent(event);
    }
}
