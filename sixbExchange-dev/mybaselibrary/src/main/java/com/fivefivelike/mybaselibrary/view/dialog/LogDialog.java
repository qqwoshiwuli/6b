package com.fivefivelike.mybaselibrary.view.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.logger.klog.KlogData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/2/3 0003.
 */

public class LogDialog extends DialogFragment {
    LogsAdapter adapter;
    List<String> logs;
    public RecyclerView recycler_view;
    public EditText edit;

    boolean isChange = true;
    String search = "";

    public void search(String search) {
        List<String> searchs = new ArrayList<>();
        for (int i = 0; i < logs.size(); i++) {
            if (logs.get(i).contains(search)) {
                searchs.add(logs.get(i));
            }
        }
        adapter.setDatas(searchs);
        isChange = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_log, container, false);
        this.recycler_view = (RecyclerView) v.findViewById(R.id.recycler_view);
        this.edit = (EditText) v.findViewById(R.id.edit);
        this.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    isChange = true;
                } else {
                    isChange = false;
                    search = s.toString();
                    search(s.toString());
                }
            }
        });
        logs = new ArrayList<>();
        logs.add("Log:");
        adapter = new LogsAdapter(getActivity(), new ArrayList<String>());
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);
        EventBus.getDefault().register(this);
        return v;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onExchangeName(KlogData event) {
        if (adapter != null && isChange) {
            logs.add(event.getMsg());
            if (!TextUtils.isEmpty(search)) {
                if (event.getMsg().contains(search)) {
                    adapter.getDatas().add(event.getMsg());
                    adapter.notifyItemInserted(adapter.getDatas().size());
                }
            } else {
                adapter.getDatas().add(event.getMsg());
                adapter.notifyItemInserted(adapter.getDatas().size());
            }

        }
    }
}
