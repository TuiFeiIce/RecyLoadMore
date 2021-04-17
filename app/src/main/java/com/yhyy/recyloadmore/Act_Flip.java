package com.yhyy.recyloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yhyy.recyloadmore.adapter.Adapter_Flip;
import com.yhyy.recyloadmore.adapter.Adapter_Vertical;
import com.yhyy.recyloadmore.inter.OnItemClickListener;
import com.yhyy.recyloadmore.widget.RecyFlip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Act_Flip extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    Adapter_Flip adapterFlip;

    List<String> stringList = new ArrayList<>();

    boolean end = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initToolBar();
        initListener();
    }

    private void initListener() {
        recyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
        adapterFlip = new Adapter_Flip(context, stringList);
        if (stringList.size() == 0) {
            adapterFlip.changeMoreStatus(Adapter_Vertical.NOTMORE);
        } else {
            adapterFlip.changeMoreStatus(Adapter_Vertical.LOADMORE);
        }
        recyclerview.setAdapter(adapterFlip);
        recyclerview.addOnScrollListener(new RecyFlip() {
            @Override
            public void onLoadMore() {
                if (end) {
                    adapterFlip.changeMoreStatus(Adapter_Vertical.LOADING);
                    end = false;
                    adapterFlip.AddFooterItem(stringList);
                } else {
                    adapterFlip.changeMoreStatus(Adapter_Vertical.NOTMORE);
                }
            }
        });
        adapterFlip.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Integer integer) {
                Toast.makeText(context, "第" + (integer + 1) + "条", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        stringList.clear();
        for (int i = 0; i < 20; i++) {
            String a = "123";
            String b = "123456";
            String c = "123456789";
            if (i % 3 == 0) {
                stringList.add(a);
            } else if (i % 3 == 1) {
                stringList.add(b);
            } else if (i % 3 == 2) {
                stringList.add(c);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}