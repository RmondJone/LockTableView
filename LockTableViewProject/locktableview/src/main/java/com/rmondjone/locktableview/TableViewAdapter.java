package com.rmondjone.locktableview;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.rmondjone.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * 说明
 * 作者 郭翰林
 * 创建时间 2017/9/17.
 */

public class TableViewAdapter extends RecyclerView.Adapter<TableViewAdapter.TableViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 第一列数据
     */
    private ArrayList<String> mLockColumnDatas;
    /**
     * 表格数据
     */
    private ArrayList<ArrayList<String>> mTableDatas;

    /**
     * 第一列是否被锁定
     */
    private boolean isLockColumn;
    /**
     * 第一行是否被锁定
     */
    private boolean isLockFristRow;
    /**
     * 记录每列最大宽度
     */
    private ArrayList<Integer> mColumnMaxWidths = new ArrayList<Integer>();
    /**
     * 记录每行最大高度
     */
    private ArrayList<Integer> mRowMaxHeights = new ArrayList<Integer>();

    /**
     * 单元格字体大小
     */
    private int mTextViewSize;
    /**
     * 第一行背景颜色
     */
    private int mFristRowBackGroudColor;
    /**
     * 表格头部字体颜色
     */
    private int mTableHeadTextColor;
    /**
     * 表格内容字体颜色
     */
    private int mTableContentTextColor;

    /**
     * 表格横向滚动监听事件
     */
    private LockTableView.OnTableViewListener mTableViewListener;

    /**
     * 表格横向滚动到边界监听事件
     */
    private LockTableView.OnTableViewRangeListener mTableViewRangeListener;

    /**
     * 表格视图加载完成监听事件
     */
    private OnTableViewCreatedListener mOnTableViewCreatedListener;

    /**
     * 锁定视图Adapter
     */
    private LockColumnAdapter mLockAdapter;

    /**
     * 未锁定视图Adapter
     */
    private UnLockColumnAdapter mUnLockAdapter;


    /**
     * 构造方法
     *
     * @param mContext
     * @param mLockColumnDatas
     * @param mTableDatas
     * @param isLockColumn
     */
    public TableViewAdapter(Context mContext, ArrayList<String> mLockColumnDatas, ArrayList<ArrayList<String>> mTableDatas, boolean isLockColumn,boolean isLockRow) {
        this.mContext = mContext;
        this.mLockColumnDatas = mLockColumnDatas;
        this.mTableDatas = mTableDatas;
        this.isLockColumn = isLockColumn;
        this.isLockFristRow=isLockRow;
    }


    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TableViewHolder mTableViewHolder = new TableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.locktablecontentview,null));
        if (mOnTableViewCreatedListener!=null){
            mOnTableViewCreatedListener.onTableViewCreatedCompleted(mTableViewHolder.mScrollView);
        }
        return mTableViewHolder;
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (isLockColumn) {
            //构造锁定视图
            holder.mLockRecyclerView.setVisibility(View.VISIBLE);
            if (mLockAdapter==null){
                mLockAdapter=new LockColumnAdapter(mContext,mLockColumnDatas);
                mLockAdapter.setRowMaxHeights(mRowMaxHeights);
                mLockAdapter.setColumnMaxWidths(mColumnMaxWidths);
                mLockAdapter.setTextViewSize(mTextViewSize);
                mLockAdapter.setLockFristRow(isLockFristRow);
                mLockAdapter.setFristRowBackGroudColor(mFristRowBackGroudColor);
                mLockAdapter.setTableHeadTextColor(mTableHeadTextColor);
                mLockAdapter.setTableContentTextColor(mTableContentTextColor);
                holder.mLockRecyclerView.setPullRefreshEnabled(false);
                holder.mLockRecyclerView.setLoadingMoreEnabled(false);
                holder.mLockRecyclerView.setLayoutManager(layoutManager);
                holder.mLockRecyclerView.addItemDecoration(new DividerItemDecoration(mContext
                        ,DividerItemDecoration.VERTICAL));
                holder.mLockRecyclerView.setAdapter(mLockAdapter);
            }else{
                mLockAdapter.notifyDataSetChanged();
            }
        } else {
            holder.mLockRecyclerView.setVisibility(View.GONE);
        }
        //构造主表格视图
        if (mUnLockAdapter==null){
            mUnLockAdapter=new UnLockColumnAdapter(mContext,mTableDatas);
            mUnLockAdapter.setColumnMaxWidths(mColumnMaxWidths);
            mUnLockAdapter.setRowMaxHeights(mRowMaxHeights);
            mUnLockAdapter.setTextViewSize(mTextViewSize);
            mUnLockAdapter.setLockFristRow(isLockFristRow);
            mUnLockAdapter.setFristRowBackGroudColor(mFristRowBackGroudColor);
            mUnLockAdapter.setTableHeadTextColor(mTableHeadTextColor);
            mUnLockAdapter.setTableContentTextColor(mTableContentTextColor);
            mUnLockAdapter.setLockFristColumn(isLockColumn);
            holder.mMainRecyclerView.setPullRefreshEnabled(false);
            holder.mMainRecyclerView.setLoadingMoreEnabled(false);
            LinearLayoutManager unlockLayoutManager = new LinearLayoutManager(mContext);
            unlockLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.mMainRecyclerView.setLayoutManager(unlockLayoutManager);
            holder.mMainRecyclerView.addItemDecoration(new DividerItemDecoration(mContext
                    ,DividerItemDecoration.VERTICAL));
            holder.mMainRecyclerView.setAdapter(mUnLockAdapter);
        }else{
            mUnLockAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class TableViewHolder extends RecyclerView.ViewHolder {
        XRecyclerView mLockRecyclerView;
        XRecyclerView mMainRecyclerView;
        CustomHorizontalScrollView mScrollView;

        public TableViewHolder(View itemView) {
            super(itemView);
            mLockRecyclerView = (XRecyclerView) itemView.findViewById(R.id.lock_recyclerview);
            mMainRecyclerView = (XRecyclerView) itemView.findViewById(R.id.main_recyclerview);
            //解决下拉刷新滑动冲突
            mLockRecyclerView.setFocusable(false);
            mMainRecyclerView.setFocusable(false);
            mScrollView = (CustomHorizontalScrollView) itemView.findViewById(R.id.lockScrollView_parent);
            mScrollView.setOnScrollChangeListener(new CustomHorizontalScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    if(mTableViewListener!=null){
                        mTableViewListener.onTableViewScrollChange(x,y);
                    }
                }

                @Override
                public void onScrollFarLeft(HorizontalScrollView scrollView) {
                    if(mTableViewRangeListener!=null){
                        mTableViewRangeListener.onLeft(scrollView);
                    }
                }

                @Override
                public void onScrollFarRight(HorizontalScrollView scrollView) {
                    if(mTableViewRangeListener!=null){
                        mTableViewRangeListener.onRight(scrollView);
                    }
                }
            });
        }
    }

    /**
     * 表格创建完成回调
     */
    public interface OnTableViewCreatedListener{
        /**
         *  返回当前横向滚动视图给上个界面
         */
         void onTableViewCreatedCompleted(CustomHorizontalScrollView mScrollView);
    }

    //取得每行每列应用高宽
    public void setColumnMaxWidths(ArrayList<Integer> mColumnMaxWidths) {
        this.mColumnMaxWidths = mColumnMaxWidths;
    }

    public void setRowMaxHeights(ArrayList<Integer> mRowMaxHeights) {
        this.mRowMaxHeights = mRowMaxHeights;
    }

    public void setTextViewSize(int mTextViewSize) {
        this.mTextViewSize = mTextViewSize;
    }

    public void setFristRowBackGroudColor(int mFristRowBackGroudColor) {
        this.mFristRowBackGroudColor = mFristRowBackGroudColor;
    }

    public void setTableHeadTextColor(int mTableHeadTextColor) {
        this.mTableHeadTextColor = mTableHeadTextColor;
    }

    public void setTableContentTextColor(int mTableContentTextColor) {
        this.mTableContentTextColor = mTableContentTextColor;
    }

    public void setHorizontalScrollView(LockTableView.OnTableViewListener mTableViewListener) {
        this.mTableViewListener = mTableViewListener;
    }

    public void setOnTableViewCreatedListener(OnTableViewCreatedListener mOnTableViewCreatedListener) {
        this.mOnTableViewCreatedListener = mOnTableViewCreatedListener;
    }

    public void setTableViewRangeListener(LockTableView.OnTableViewRangeListener mTableViewRangeListener) {
        this.mTableViewRangeListener = mTableViewRangeListener;
    }
}
