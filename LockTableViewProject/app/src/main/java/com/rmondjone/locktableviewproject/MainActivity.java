package com.rmondjone.locktableviewproject;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentView = (LinearLayout) findViewById(R.id.contentView);
        initDisplayOpinion();

        //构造假数据
        ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
        ArrayList<String> mfristData = new ArrayList<String>();
        mfristData.add("标题");
        for (int i = 0; i < 10; i++) {
            mfristData.add("标题" + i);
        }
        mTableDatas.add(mfristData);
        for (int i = 0; i < 20; i++) {
            ArrayList<String> mRowDatas = new ArrayList<String>();
            mRowDatas.add("标题" + i);
            for (int j = 0; j < 10; j++) {
                mRowDatas.add("数据" + j);
            }
            mTableDatas.add(mRowDatas);
        }
        final LockTableView mLockTableView = new LockTableView(this, mContentView, mTableDatas);
        Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
        mLockTableView.setLockFristColumn(true) //是否锁定第一列
                .setLockFristRow(true) //是否锁定第一行
                .setMaxColumnWidth(100) //列最大宽度
                .setMinColumnWidth(60) //列最小宽度
                .setColumnWidth(1,30) //设置指定列文本宽度
                .setColumnWidth(2,20)
                .setMinRowHeight(20)//行最小高度
                .setMaxRowHeight(60)//行最大高度
                .setTextViewSize(16) //单元格字体大小
                .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                .setTableContentTextColor(R.color.border_color)//单元格字体颜色
                .setCellPadding(15)//设置单元格内边距(dp)
                .setNullableString("N/A") //空值替换值
                .setTableViewListener(new LockTableView.OnTableViewListener() {
                    @Override
                    public void onTableViewScrollChange(int x, int y) {
//                        Log.e("滚动值","["+x+"]"+"["+y+"]");
                    }
                })//设置横向滚动回调监听
                .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                    @Override
                    public void onLeft(HorizontalScrollView view) {
                        Log.e("滚动边界","滚动到最左边");
                    }

                    @Override
                    public void onRight(HorizontalScrollView view) {
                        Log.e("滚动边界","滚动到最右边");
                    }
                })//设置横向滚动边界监听
                .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                    @Override
                    public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                        Log.e("onRefresh",Thread.currentThread().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                Log.e("现有表格数据", mTableDatas.toString());
                                //构造假数据
                                ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
                                ArrayList<String> mfristData = new ArrayList<String>();
                                mfristData.add("标题");
                                for (int i = 0; i < 10; i++) {
                                    mfristData.add("标题" + i);
                                }
                                mTableDatas.add(mfristData);
                                for (int i = 0; i < 20; i++) {
                                    ArrayList<String> mRowDatas = new ArrayList<String>();
                                    mRowDatas.add("标题" + i);
                                    for (int j = 0; j < 10; j++) {
                                        mRowDatas.add("数据" + j);
                                    }
                                    mTableDatas.add(mRowDatas);
                                }
                                mLockTableView.setTableDatas(mTableDatas);
                                mXRecyclerView.refreshComplete();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                        Log.e("onLoadMore",Thread.currentThread().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mTableDatas.size() <= 60) {
                                    for (int i = 0; i < 10; i++) {
                                        ArrayList<String> mRowDatas = new ArrayList<String>();
                                        mRowDatas.add("标题" + (mTableDatas.size() - 1));
                                        for (int j = 0; j < 10; j++) {
                                            mRowDatas.add("数据" + j);
                                        }
                                        mTableDatas.add(mRowDatas);
                                    }
                                    mLockTableView.setTableDatas(mTableDatas);
                                } else {
                                    mXRecyclerView.setNoMore(true);
                                }
                                mXRecyclerView.loadMoreComplete();
                            }
                        }, 1000);
                    }
                })
                .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                    @Override
                    public void onItemClick(View item, int position) {
                        Log.e("点击事件",position+"");
                    }
                })
                .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                    @Override
                    public void onItemLongClick(View item, int position) {
                        Log.e("长按事件",position+"");
                    }
                })
                .setOnItemSeletor(R.color.dashline_color)//设置Item被选中颜色
                .show(); //显示表格,此方法必须调用
        mLockTableView.getTableScrollView().setPullRefreshEnabled(true);
        mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);
        mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
        //属性值获取
        Log.e("每列最大宽度(dp)", mLockTableView.getColumnMaxWidths().toString());
        Log.e("每行最大高度(dp)", mLockTableView.getRowMaxHeights().toString());
        Log.e("表格所有的滚动视图", mLockTableView.getScrollViews().toString());
        Log.e("表格头部固定视图(锁列)", mLockTableView.getLockHeadView().toString());
        Log.e("表格头部固定视图(不锁列)", mLockTableView.getUnLockHeadView().toString());
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
