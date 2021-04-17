# RecyLoadMore
RecyclerView加载更多

主要是重写RecyclerView.OnScrollListener

不同布局获取最后一条数据，实现加载更多

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
