package com.pingxundata.pxcore.metadata.listener;

/**
 * Created by Administrator on 2017/7/1.
 */

public interface RefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();

}