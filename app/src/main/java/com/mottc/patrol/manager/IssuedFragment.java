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

public class IssuedFragment extends Fragment {


    private OnIssuedClickListener mListener;
    private List<String> staffList;
    private IssuedRecyclerViewAdapter mIssuedRecyclerViewAdapter;

    private static IssuedFragment sIssuedFragment = new IssuedFragment();

    public IssuedFragment() {
    }


    public static IssuedFragment newInstance() {
        return sIssuedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issued, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            mIssuedRecyclerViewAdapter = new IssuedRecyclerViewAdapter(staffList, mListener);

            recyclerView.setAdapter(mIssuedRecyclerViewAdapter);
        }

        updateIssuedList();
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIssuedClickListener) {
            mListener = (OnIssuedClickListener) context;
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


    public void updateIssuedList() {
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                staffList.clear();
                staffList.addAll(value);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIssuedRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }
    public interface OnIssuedClickListener {
        // TODO: Update argument type and name
        void issuedClick(String item);
    }
}
