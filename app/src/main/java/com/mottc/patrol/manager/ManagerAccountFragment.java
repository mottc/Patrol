package com.mottc.patrol.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mottc.patrol.R;

public class ManagerAccountFragment extends Fragment {


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_account, container, false);
    }

}
