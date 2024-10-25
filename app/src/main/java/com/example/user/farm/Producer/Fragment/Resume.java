package com.example.user.farm.Producer.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.farm.Producer.Adapter.Cropfarm;
import com.example.user.farm.Producer.Adapter.ResumeAdapter;
import com.example.user.farm.R;

/**
 * Created by user on 2018/3/16.
 */

public class Resume extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produce_resume, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        // 設定 RecylerView 使用的 Adapter 物件
        recyclerView.setAdapter(new ResumeAdapter(recyclerView.getContext(), R.layout.item_linear_resume));


        return view;
    }
}
