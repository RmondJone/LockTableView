package com.rmondjone.locktableview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明
 * 作者 郭翰林
 * 创建时间 2017/9/17.
 */

public class UnLockColumnAdapter extends RecyclerView.Adapter<UnLockColumnAdapter.UnLockViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 表格数据
     */
    private ArrayList<ArrayList<String>> mTableDatas;
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
     * 是否锁定首行
     */
    private boolean isLockFristRow = true;
    /**
     * 是否锁定首列
     */
    private boolean isLockFristColumn = true;

    /**
     * 单元格内边距
     */
    private int mCellPadding;

    /**
     * Item点击事件
     */
    private LockTableView.OnItemClickListenter mOnItemClickListenter;

    /**
     * Item长按事件
     */
    private LockTableView.OnItemLongClickListenter mOnItemLongClickListenter;

    /**
     * Item项被选中监听(处理被选中的效果)
     */
    private TableViewAdapter.OnItemSelectedListenter mOnItemSelectedListenter;

    /**
     * 构造方法
     *
     * @param mContext
     * @param mTableDatas
     */
    public UnLockColumnAdapter(Context mContext, ArrayList<ArrayList<String>> mTableDatas) {
        this.mContext = mContext;
        this.mTableDatas = mTableDatas;
    }


    @Override
    public int getItemCount() {
        return mTableDatas.size();
    }

    @Override
    public UnLockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UnLockViewHolder holder = new UnLockViewHolder(LayoutInflater.from(mContext).inflate(R.layout.unlock_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(UnLockViewHolder holder, final int position) {
        ArrayList<String> datas = mTableDatas.get(position);
        if (isLockFristRow) {
            //第一行是锁定的
            createRowView(holder.mLinearLayout, datas, false, mRowMaxHeights.get(position + 1));
        } else {
            if (position == 0) {
                holder.mLinearLayout.setBackgroundColor(ContextCompat.getColor(mContext, mFristRowBackGroudColor));
                createRowView(holder.mLinearLayout, datas, true, mRowMaxHeights.get(position));
            } else {
                createRowView(holder.mLinearLayout, datas, false, mRowMaxHeights.get(position));
            }
        }
        //添加事件
        if(mOnItemClickListenter!=null){
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemSelectedListenter!=null){
                        mOnItemSelectedListenter.onItemSelected(v,position);
                    }
                    if(isLockFristRow){
                        mOnItemClickListenter.onItemClick(v,position+1);
                    }else{
                        if(position!=0){
                            mOnItemClickListenter.onItemClick(v,position);
                        }
                    }
                }
            });
        }
        if(mOnItemLongClickListenter!=null){
            holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mOnItemSelectedListenter!=null){
                        mOnItemSelectedListenter.onItemSelected(v,position);
                    }
                    if (isLockFristRow){
                        mOnItemLongClickListenter.onItemLongClick(v,position+1);
                    }else{
                        if(position!=0){
                            mOnItemLongClickListenter.onItemLongClick(v,position);
                        }
                    }
                    return true;
                }
            });
        }
        //如果没有设置点击事件和长按事件
        if(mOnItemClickListenter==null&&mOnItemLongClickListenter==null){
            holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemSelectedListenter!=null){
                        mOnItemSelectedListenter.onItemSelected(v,position);
                    }
                }
            });
            holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mOnItemSelectedListenter!=null){
                        mOnItemSelectedListenter.onItemSelected(v,position);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

    public void setLockFristColumn(boolean lockFristColumn) {
        isLockFristColumn = lockFristColumn;
    }

    public void setOnItemClickListenter(LockTableView.OnItemClickListenter mOnItemClickListenter) {
        this.mOnItemClickListenter = mOnItemClickListenter;
    }

    public void setOnItemLongClickListenter(LockTableView.OnItemLongClickListenter mOnItemLongClickListenter) {
        this.mOnItemLongClickListenter = mOnItemLongClickListenter;
    }

    public void setOnItemSelectedListenter(TableViewAdapter.OnItemSelectedListenter mOnItemSelectedListenter) {
        this.mOnItemSelectedListenter = mOnItemSelectedListenter;
    }

    public void setCellPadding(int mCellPadding) {
        this.mCellPadding = mCellPadding;
    }

    class UnLockViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;

        public UnLockViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.unlock_linearlayout);
        }
    }

    /**
     * 构造每行数据视图
     *
     * @param linearLayout
     * @param datas
     * @param isFristRow   是否是第一行
     */
    private void createRowView(LinearLayout linearLayout, List<String> datas, boolean isFristRow, int mMaxHeight) {
        //设置LinearLayout
        linearLayout.removeAllViews();//首先清空LinearLayout,复用会造成重复绘制，使内容超出预期长度
        for (int i = 0; i < datas.size(); i++) {
            //构造单元格
            TextView textView = new TextView(mContext);
            if (isFristRow) {
                textView.setTextColor(ContextCompat.getColor(mContext, mTableHeadTextColor));
            } else {
                textView.setTextColor(ContextCompat.getColor(mContext, mTableContentTextColor));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextViewSize);
            textView.setGravity(Gravity.CENTER);
            textView.setText(datas.get(i));
            //设置布局
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(mCellPadding, mCellPadding, mCellPadding, mCellPadding);
            textViewParams.height = DisplayUtil.dip2px(mContext, mMaxHeight);
            if (isLockFristColumn) {
                textViewParams.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(i+1));
            } else {
                textViewParams.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(i));
            }
            textView.setLayoutParams(textViewParams);
            linearLayout.addView(textView);
            //画分隔线
            if (i != datas.size() - 1) {
                View splitView = new View(mContext);
                ViewGroup.LayoutParams splitViewParmas = new ViewGroup.LayoutParams(DisplayUtil.dip2px(mContext, 1),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                splitView.setLayoutParams(splitViewParmas);
                if (isFristRow) {
                    splitView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    splitView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_gray));
                }
                linearLayout.addView(splitView);
            }
        }
    }
}
