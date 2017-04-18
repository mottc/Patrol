package com.mottc.patrol.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.mottc.patrol.R;

import java.util.ArrayList;
import java.util.List;

public class ExamineFragment extends Fragment {


    private OnExamineClickListener mListener;
    private List<String> mStaffs;
    private ExamineRecyclerViewAdapter mExamineRecyclerViewAdapter;

    private static ExamineFragment sExamineFragment = new ExamineFragment();



    public ExamineFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExamineFragment newInstance() {


        return sExamineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStaffs = new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_examine, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            mExamineRecyclerViewAdapter = new ExamineRecyclerViewAdapter(mStaffs, mListener);
            recyclerView.setAdapter(mExamineRecyclerViewAdapter);
        }


        updateStaffList();


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExamineClickListener) {
            mListener = (OnExamineClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateStaffList() {

        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                mStaffs.clear();
                mStaffs.addAll(value);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mExamineRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }


    public interface OnExamineClickListener {
        // TODO: Update argument type and name
        void onExamineClick(String item);
    }
}
