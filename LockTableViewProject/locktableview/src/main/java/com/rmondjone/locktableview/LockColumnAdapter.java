package com.rmondjone.locktableview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 说明
 * 作者 郭翰林
 * 创建时间 2017/9/17.
 */

public class LockColumnAdapter extends RecyclerView.Adapter<LockColumnAdapter.LockViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 第一列数据
     */
    private ArrayList<String> mLockColumnDatas;
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
     * 是否锁定首行
     */
    private boolean isLockFristRow = true;
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

    public LockColumnAdapter(Context mContext, ArrayList<String> mLockColumnDatas) {
        this.mContext = mContext;
        this.mLockColumnDatas = mLockColumnDatas;
    }


    @Override
    public LockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LockViewHolder holder = new LockViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lock_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(LockViewHolder holder, int position) {
        //设置布局
        holder.mTextView.setText(mLockColumnDatas.get(position));
        holder.mTextView.setTextSize(mTextViewSize);
        ViewGroup.LayoutParams layoutParams = holder.mTextView.getLayoutParams();
        layoutParams.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(0));
        if (isLockFristRow){
            layoutParams.height=DisplayUtil.dip2px(mContext,mRowMaxHeights.get(position+1));
        }else{
            layoutParams.height=DisplayUtil.dip2px(mContext,mRowMaxHeights.get(position));
        }
        holder.mTextView.setLayoutParams(layoutParams);
        //设置颜色
        if (!isLockFristRow){
            if (position==0){
                holder.mLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext,mFristRowBackGroudColor));
                holder.mTextView.setTextColor(ContextCompat.getColor(mContext, mTableHeadTextColor));
            }else{
                holder.mTextView.setTextColor(ContextCompat.getColor(mContext, mTableContentTextColor));
            }
        }else{
            holder.mTextView.setTextColor(ContextCompat.getColor(mContext, mTableContentTextColor));
        }
    }

    @Override
    public int getItemCount() {
        return mLockColumnDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class LockViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        LinearLayout mLinearLayout;
        public LockViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.lock_text);
            mLinearLayout=(LinearLayout)itemView.findViewById(R.id.lock_linearlayout);
        }
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

    public void setLockFristRow(boolean lockFristRow) {
        isLockFristRow = lockFristRow;
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
}
