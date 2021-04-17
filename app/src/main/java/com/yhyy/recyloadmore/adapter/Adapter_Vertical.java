package com.yhyy.recyloadmore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yhyy.recyloadmore.R;
import com.yhyy.recyloadmore.inter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Vertical extends RecyclerView.Adapter {
    public OnItemClickListener onItemClickListener;
    public LayoutInflater layoutInflater;
    private Context context;
    private List<String> stringList;

    //上拉加载更多
    public static final int LOADMORE = 0;
    //正在加载中
    public static final int LOADING = 1;
    //没有加载更多
    public static final int NOTMORE = 2;
    //上拉加载更多状态-默认为2
    private int mLoadMoreStatus = 2;

    public Adapter_Vertical(Context context, List<String> datas) {
        this.context = context;
        this.stringList = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new LinearHolder(layoutInflater.inflate(R.layout.recy_vertical, parent, false));
        } else {
            return new FooterHolder(layoutInflater.inflate(R.layout.include_control_loadmore, parent, false));
        }
    }

    //解决 GridLayoutManager 加载更多时，加载动画最后一个item没有占满屏幕宽度问题
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (stringList.size() == 0 || stringList.size() == position) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    //解决 StaggeredGridLayoutManager 加载更多时，加载动画最后一个item没有占满屏幕宽度问题
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            int position = holder.getLayoutPosition();
            if (position == stringList.size()) {
                params.setFullSpan(true);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LinearHolder) {
            LinearHolder linearHolder = (LinearHolder) holder;
            linearHolder.tvItemName.setText(stringList.get(position));
            linearHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(linearHolder.itemView, position);
                }
            });
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            switch (mLoadMoreStatus) {
                case LOADMORE:
                    footerHolder.progressbar.setVisibility(View.GONE);
                    footerHolder.tvLoad.setText("上拉加载更多...");
                    break;
                case LOADING:
                    footerHolder.progressbar.setVisibility(View.VISIBLE);
                    footerHolder.tvLoad.setText("加载中...");
                    break;
                case NOTMORE:
                    footerHolder.progressbar.setVisibility(View.GONE);
                    footerHolder.tvLoad.setText("没有更多了");
                    footerHolder.tvLoad.setTextColor(ContextCompat.getColor(context, R.color.gray_text));
                    break;
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == stringList.size()) {
            //最后一个 是底部item
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size() + 1;
    }

    class LinearHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_name)
        TextView tvItemName;

        public LinearHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.tv_load)
        TextView tvLoad;

        public FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void AddFooterItem(List<String> items) {
        stringList.addAll(items);
        notifyDataSetChanged();
    }

    public void AddHeaderItem(List<String> items) {
        stringList.addAll(0, items);
        notifyDataSetChanged();
    }

    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }
}