package com.yhyy.recyloadmore.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceWolf on 2019/9/13.
 */
public class Contracts {
    public static final List<String> typeList = new ArrayList<String>() {//设置列表
        {
            add(new String("网格加载更多切换"));
            add(new String("线性加载更多"));
            add(new String("瀑布流加载更多"));
            add(new String("线性翻转"));
        }
    };
}
