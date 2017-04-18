package com.mottc.patrol.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.choose.ChooseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AccountFragment extends Fragment {


    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.boss_name)
    TextView mBossName;
    @BindView(R.id.quit)
    Button mQuit;
    Unbinder unbinder;

    private String username;

    public AccountFragment() {
        // Required empty public constructor
    }

    private static AccountFragment sAccountFragment = new AccountFragment();
    public static AccountFragment newInstance() {


        return sAccountFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = PatrolApplication.getInstance().getCurrentUserName();
        username = username.substring(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUsername.setText(username);
        setManagerName();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.quit)
    public void onViewClicked() {

        logout();
    }

    public void setManagerName() {

        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(final List<String> value) {
                if (value.size() != 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBossName.setText(value.get(0).substring(8));
                        }
                    });
                } else {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBossName.setText("尚无管理者");
                        }
                    });
                }

            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }

    private void logout() {
        PatrolApplication.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                getActivity().finish();
                startActivity(new Intent(getActivity(), ChooseActivity.class));
            }

            @Override
            public void onError(int code, String error) {

                Toast.makeText(getActivity(), "请重试！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
