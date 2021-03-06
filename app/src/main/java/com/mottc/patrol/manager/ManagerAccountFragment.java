package com.mottc.patrol.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ManagerAccountFragment extends Fragment {


    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.damage_bt)
    Button mDamageBt;

    Unbinder unbinder;


    private String username;

    public ManagerAccountFragment() {
        // Required empty public constructor
    }

    public static ManagerAccountFragment newInstance() {
        ManagerAccountFragment fragment = new ManagerAccountFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = PatrolApplication.getInstance().getCurrentUserName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        mUsername.setText(username.substring(8));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.damage_bt)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), ShowImagesActivity.class));

    }
}
