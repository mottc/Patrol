package com.mottc.patrol.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mottc.patrol.Constant;
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
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(linearLayoutManager);
            mExamineRecyclerViewAdapter = new ExamineRecyclerViewAdapter(mStaffs, mListener);
            recyclerView.setAdapter(mExamineRecyclerViewAdapter);
        }


//        updateStaffList(null,null);


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

    public void updateStaffList(String user,String status) {

        if (status.equals(Constant.ONLINE)) {
            if (!mStaffs.contains(user)) {
                mStaffs.add(user);
            }
        } else if (status.equals(Constant.OFFLINE)) {
            mStaffs.remove(user);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mExamineRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

//        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
//            @Override
//            public void onSuccess(List<String> value) {
//                mStaffs.clear();
//                mStaffs.addAll(value);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mExamineRecyclerViewAdapter.notifyDataSetChanged();
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onError(int error, String errorMsg) {
//
//            }
//        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mStaffs.clear();
    }

    public interface OnExamineClickListener {
        // TODO: Update argument type and name
        void onExamineClick(String item);
    }
}
