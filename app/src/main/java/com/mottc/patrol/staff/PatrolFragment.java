package com.mottc.patrol.staff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mottc.patrol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.mottc.patrol.R.id.upload;


public class PatrolFragment extends Fragment {


    @BindView(upload)
    Button mUpload;
    @BindView(R.id.help)
    Button mHelp;
    Unbinder unbinder;

    public PatrolFragment() {
        // Required empty public constructor
    }
    public static PatrolFragment newInstance() {
        PatrolFragment fragment = new PatrolFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patrol, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({upload, R.id.help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case upload:
                uploadPhoto();
                break;
            case R.id.help:
                callForHelp();
                break;
        }
    }

    private void callForHelp() {

    }

    private void uploadPhoto() {

    }
}
