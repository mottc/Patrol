package com.mottc.patrol.staff;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.mottc.patrol.Constant;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mottc.patrol.R.string.task;

public class StaffActivity extends AppCompatActivity implements TaskFragment.OnTaskListClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bnv_menu)
    BottomNavigationView mBnvMenu;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private MenuItem mMenuItem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        ButterKnife.bind(this);
        mToolbar.setTitle("巡更");
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

//        Location location = new Location();

        sendStatusInfo(Constant.ONLINE);
        EMClient.getInstance().contactManager().setContactListener(new PatrolStaffContactListener());
        EMClient.getInstance().chatManager().addMessageListener(new PatrolTaskAddListener());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendStatusInfo(Constant.OFFLINE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PatrolFragment.newInstance(),getString(R.string.patrol));
        adapter.addFragment(TaskFragment.newInstance(),getString(task));
        adapter.addFragment(AccountFragment.newInstance(),getString(R.string.account));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void OnTaskListClick(Task item) {
        startActivity(new Intent(this, TaskDetailActivity.class).putExtra("taskId", item.getId()));
    }

    private void sendStatusInfo(final String status) {
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                if (value.size() != 0) {
                    EMMessage message = EMMessage.createTxtSendMessage(status, value.get(0));
                    EMClient.getInstance().chatManager().sendMessage(message);
                }
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    private class PatrolTaskAddListener implements EMMessageListener{
        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                Task task = new Task();

                task.setExecutor(PatrolApplication.getInstance().getCurrentUserName());
                task.setAnnouncer(message.getFrom());
                task.setStatus(Constant.TASK_STATUS_UNDONE);
                try {
                    JSONObject content = new JSONObject(((EMTextMessageBody)message.getBody()).getMessage());
                    task.setTime(content.getString("time"));
                    task.setLocation(content.getString("location"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PatrolApplication.getInstance().getDaoSession().getTaskDao().insert(task);
                TaskFragment.newInstance().updateTaskList();
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

    }

    private class PatrolStaffContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {

            AccountFragment.newInstance().setManagerName();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getParent())
                   .setContentTitle("您已成为"+username+"的员工")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify((int) System.currentTimeMillis(), builder.build());
        }

        @Override
        public void onContactDeleted(String username) {
            AccountFragment.newInstance().setManagerName();
        }

        @Override
        public void onContactInvited(final String username, String reason) {

            EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
                @Override
                public void onSuccess(List<String> value) {
                    if (value.size() == 0) {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(username);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            EMClient.getInstance().contactManager().declineInvitation(username);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(int error, String errorMsg) {

                }
            });

        }

        @Override
        public void onFriendRequestAccepted(String username) {

        }

        @Override
        public void onFriendRequestDeclined(String username) {

        }
    }
}

