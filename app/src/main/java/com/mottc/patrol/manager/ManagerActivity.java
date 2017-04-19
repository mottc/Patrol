package com.mottc.patrol.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.mottc.patrol.Constant;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.choose.ChooseActivity;
import com.mottc.patrol.issued.IssuedActivity;
import com.mottc.patrol.staff.ViewPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends AppCompatActivity implements IssuedFragment.OnIssuedClickListener, ExamineFragment.OnExamineClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.bnv_menu)
    BottomNavigationView mBnvMenu;
    @BindView(R.id.more)
    ImageView mMore;

    private MenuItem mMenuItem = null;
    private PatrolManagerContactListener mPatrolManagerContactListener;
    private PatrolMessageListener mPatrolMessageListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        mToolbar.setTitle("管理");
        setSupportActionBar(mToolbar);
        setupViewPager(mViewpager);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBnvMenu.getMenu().getItem(0).setChecked(false);
                }
                mBnvMenu.getMenu().getItem(position).setChecked(true);
                mMenuItem = mBnvMenu.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBnvMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_patrol:
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.action_task:
                        mViewpager.setCurrentItem(1);

                        break;
                    case R.id.action_me:
                        mViewpager.setCurrentItem(2);

                        break;
                }
                return true;
            }
        });

        mPatrolManagerContactListener = new PatrolManagerContactListener();
        mPatrolMessageListener = new PatrolMessageListener();
        EMClient.getInstance().chatManager().addMessageListener(mPatrolMessageListener);
        EMClient.getInstance().contactManager().setContactListener(mPatrolManagerContactListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mPatrolMessageListener);
        EMClient.getInstance().contactManager().removeContactListener(mPatrolManagerContactListener);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ExamineFragment.newInstance(), getString(R.string.examine));
        adapter.addFragment(IssuedFragment.newInstance(), getString(R.string.issued));
        adapter.addFragment(ManagerAccountFragment.newInstance(), getString(R.string.account));
        viewPager.setAdapter(adapter);
    }


    @Override
    public void issuedClick(String item) {
        startActivity(new Intent(this, IssuedActivity.class).putExtra("username", item));
    }

    @Override
    public void onExamineClick(String item) {
        startActivity(new Intent(this, MapActivity.class).putExtra("username", item));
    }

    @OnClick(R.id.more)
    public void onViewClicked() {

        final String items[] = {"删除员工", "添加员工", "退出登录"};
        new AlertDialog.Builder(ManagerActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                final EditText editText = new EditText(ManagerActivity.this);
                                new AlertDialog.Builder(ManagerActivity.this)
                                        .setTitle("请输入要删除的员工用户名：")
                                        .setView(editText)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String username = editText.getText().toString().trim();
                                                if (TextUtils.isEmpty(username)) {
                                                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                                                }


                                                EMClient.getInstance().contactManager().aysncDeleteContact("staff_" + username, new EMCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(ManagerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onError(int code, String error) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(ManagerActivity.this, "请重试", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onProgress(int progress, String status) {

                                                    }
                                                });


                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();

                                break;
                            case 1:

                                final EditText editText1 = new EditText(ManagerActivity.this);
                                new AlertDialog.Builder(ManagerActivity.this)
                                        .setTitle("请输入要添加的员工用户名：")
                                        .setView(editText1)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String username = editText1.getText().toString().trim();
                                                if (TextUtils.isEmpty(username)) {
                                                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();

                                                }

                                                EMClient.getInstance().contactManager().aysncAddContact("staff_" + username, "", new EMCallBack() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError(int code, String error) {
                                                        Toast.makeText(ManagerActivity.this, "请重试", Toast.LENGTH_SHORT).show();

                                                    }

                                                    @Override
                                                    public void onProgress(int progress, String status) {

                                                    }
                                                });


                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                break;
                            case 2:

                                logout();

                                break;

                        }
                    }
                })
                .create().show();
    }


    private void logout() {
        PatrolApplication.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                startActivity(new Intent(ManagerActivity.this, ChooseActivity.class));
            }

            @Override
            public void onError(int code, String error) {

                Toast.makeText(ManagerActivity.this, "请重试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private class PatrolManagerContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {

            IssuedFragment.newInstance().updateIssuedList();


            Notification.Builder builder = new Notification.Builder(getParent());


            builder.setTicker("hello");//手机状态栏的提示；

            builder.setWhen(System.currentTimeMillis());//设置时间

            builder.setContentTitle("通知栏通知");//设置标题

            builder.setContentText("我来自NotificationDemo");//设置通知内容


            builder.setDefaults(Notification.DEFAULT_ALL);//设置震动

            Notification notification = builder.build();//4.1以上


            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify((int) System.currentTimeMillis(), notification);


//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getParent())
//                    .setContentTitle(username+"已成为您的员工")
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true);
//            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify((int) System.currentTimeMillis(), builder.build());
        }

        @Override
        public void onContactDeleted(String username) {

        }

        @Override
        public void onContactInvited(String username, String reason) {

        }

        @Override
        public void onFriendRequestAccepted(String username) {

        }

        @Override
        public void onFriendRequestDeclined(String username) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getParent())
                    .setContentTitle(username + "已有管理者，不能添加为员工")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify((int) System.currentTimeMillis(), builder.build());

        }
    }

    private class PatrolMessageListener implements EMMessageListener {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (final EMMessage message : messages) {
                if (message.getType() == EMMessage.Type.TXT) {
                    if (((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.ONLINE)) {
                        ExamineFragment.newInstance().updateStaffList(message.getFrom(), Constant.ONLINE);
                    } else if (((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.OFFLINE)) {
                        ExamineFragment.newInstance().updateStaffList(message.getFrom(), Constant.OFFLINE);
                    } else if (((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.ASK_FOR_HELP)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog(message.getFrom());
                            }
                        });
                    }
                } else if (message.getType() == EMMessage.Type.IMAGE) {
                    Toast.makeText(ManagerActivity.this, ((EMImageMessageBody) message.getBody()).getThumbnailUrl()
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }


        private void showDialog(String username) {
            Log.i("PatrolMessageListener", "showDialog: " + "收到警告！");
            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
            builder.setTitle("警告！");//设置标题
            builder.setMessage(username.substring(6) + "遇到危险！");//设置内容
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();//获取dialog
            dialog.show();//显示对话框
        }

    }
}

