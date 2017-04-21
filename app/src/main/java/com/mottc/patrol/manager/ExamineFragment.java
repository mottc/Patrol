package com.mottc.patrol.manager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.mottc.patrol.Constant;
import com.mottc.patrol.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
            initOnlineStaffList();
            mExamineRecyclerViewAdapter = new ExamineRecyclerViewAdapter(mStaffs, mListener);
            recyclerView.setAdapter(mExamineRecyclerViewAdapter);
        }


//        updateStaffList(null,null);
        initOnlineStaffList();


        return view;
    }

    private void initOnlineStaffList() {
        List<EMConversation> emConversations = loadConversationList();
        for (EMConversation emConversation:emConversations) {
            List<EMMessage> emMessages = emConversation.getAllMessages();
            for (EMMessage message : emMessages) {
                if (message.getType().equals(EMMessage.Type.TXT)) {
                    if (((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.ONLINE)) {
                        mStaffs.add(message.getFrom());
                        return;
                    } else if (((EMTextMessageBody) message.getBody()).getMessage().equals(Constant.OFFLINE)) {
                        return;
                    }
                }
            }
        }
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

    }



    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
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
