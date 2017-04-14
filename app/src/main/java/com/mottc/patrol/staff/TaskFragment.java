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
import java.util.List;

public class TaskFragment extends Fragment {


    private DaoSession mDaoSession;
    private List taskList;
    private OnTaskListClickListener mListener;

    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = new ArrayList();
        mDaoSession = PatrolApplication.getInstance().getDaoSession();
        getAllTasks();
    }

    private void getAllTasks() {
        List list = mDaoSession.getTaskDao().queryBuilder().where(TaskDao.Properties.Executor.eq("")).list();
        taskList.clear();
        taskList.addAll(list);
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
            recyclerView.setAdapter(new TaskRecyclerViewAdapter(taskList, mListener));
        }
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTaskListClickListener {
        // TODO: Update argument type and name
        void OnTaskListClick(Task item);
    }
}
