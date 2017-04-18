package com.mottc.patrol.staff;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mottc.patrol.PatrolApplication;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.Task;
import com.mottc.patrol.data.source.local.DaoSession;
import com.mottc.patrol.data.source.local.TaskDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskFragment extends Fragment {


    private DaoSession mDaoSession;
    private List taskList;
    private OnTaskListClickListener mListener;
    private TaskRecyclerViewAdapter mTaskRecyclerViewAdapter;

    public TaskFragment() {
    }

    private static TaskFragment sTaskFragment = new TaskFragment();
            // TODO: Customize parameter initialization

    public static TaskFragment newInstance() {

        return sTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = new ArrayList();
        mDaoSession = PatrolApplication.getInstance().getDaoSession();
        getAllTasks();
        mTaskRecyclerViewAdapter = new TaskRecyclerViewAdapter(taskList, mListener);
    }

    private void getAllTasks() {
        String username = PatrolApplication.getInstance().getCurrentUserName();
        List list = mDaoSession.getTaskDao().queryBuilder().where(TaskDao.Properties.Executor.eq(username)).list();
        taskList.clear();
        taskList.addAll(list);
        Collections.reverse(taskList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mTaskRecyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskListClickListener) {
            mListener = (OnTaskListClickListener) context;
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

    public void updateTaskList() {
        getAllTasks();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnTaskListClickListener {
        // TODO: Update argument type and name
        void OnTaskListClick(Task item);
    }
}
