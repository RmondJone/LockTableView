package com.rmondjone.locktableview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明 可锁定首行和首列的表格视图
 * 作者 郭翰林
 * 创建时间 2017/3/29.
 */

public class LockTableView {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 表格父视图
     */
    private ViewGroup mContentView;
    /**
     * 表格数据，每一行为一条数据，从表头计算
     */
    private ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
    /**
     * 表格视图
     */
    private View mTableView;
    /**
     * 是否锁定首行
     */
    private boolean isLockFristRow = true;
    /**
     * 是否锁定首列
     */
    private boolean isLockFristColumn = true;
    /**
     * 最大列宽(dp)
     */
    private int maxColumnWidth;
    /**
     * 最小列宽(dp)
     */
    private int minColumnWidth;
    /**
     * 第一行背景颜色
     */
    private int mFristRowBackGroudColor;
    /**
     * 数据为空时的缺省值
     */
    private String mNullableString;
    /**
     * 单元格字体大小
     */
    private int mTextViewSize;
    /**
     * 表格头部字体颜色
     */
    private int mTableHeadTextColor;
    /**
     * 表格内容字体颜色
     */
    private int mTableContentTextColor;


    //表格数据
    /**
     * 表格第一行数据,不包括第一个元素
     */
    private ArrayList<String> mTableFristData = new ArrayList<>();
    /**
     * 表格第一列数据，不包括第一个元素
     */
    private ArrayList<String> mTableColumnDatas = new ArrayList<>();
    /**
     * 表格左上角数据
     */
    private String mColumnTitle;
    /**
     * 表格每一行数据，不包括第一行和第一列
     */
    private ArrayList<ArrayList<String>> mTableRowDatas = new ArrayList<ArrayList<String>>();
    /**
     * 记录每列最大宽度
     */
    private ArrayList<Integer> mColumnMaxWidths = new ArrayList<Integer>();
    /**
     * 把所有的滚动视图放图列表，后面实现联动效果
     */
    private ArrayList<HorizontalScrollView> mScrollViews = new ArrayList<HorizontalScrollView>();


    //表格视图
    /**
     * 表格左上角视图
     */
    private TextView mColumnTitleView;
    /**
     * 第一行布局（锁状态）
     */
    private LinearLayout mLockHeadView;
    /**
     * 第一行布局（未锁状态）
     */
    private LinearLayout mUnLockHeadView;
    /**
     * 第一行滚动视图（锁状态）
     */
    private CustomHorizontalScrollView mLockScrollView;
    /**
     * 第一行滚动视图（未锁状态）
     */
    private CustomHorizontalScrollView mUnLockScrollView;
    /**
     * 表格主视图
     */
    private ScrollView mTableScrollView;


    /**
     * 构造方法
     *
     * @param mContext 上下文
     * @param mContentView 表格父视图
     * @param mTableDatas 表格数据
     */
    public LockTableView(Context mContext, ViewGroup mContentView, ArrayList<ArrayList<String>> mTableDatas) {
        this.mContext = mContext;
        this.mContentView = mContentView;
        this.mTableDatas = mTableDatas;
        initAttrs();
    }

    /**
     * 初始化属性
     */
    private void initAttrs() {
        mTableView = LayoutInflater.from(mContext).inflate(R.layout.locktableview, null);
        maxColumnWidth = 100;
        minColumnWidth = 70;
        mNullableString = "N/A";
        mTableHeadTextColor = R.color.beijin;
        mTableContentTextColor = R.color.border_color;
        mFristRowBackGroudColor = R.color.table_head;
        mTextViewSize = 16;
    }

    /**
     * 展现视图
     */
    public void show() {
        initData();
        initView();
        mContentView.addView(mTableView);
    }


    /**
     * 初始化表格数据
     */
    private void initData() {
        if (mTableDatas != null && mTableDatas.size() > 0) {
            //检查数据，如果有一行数据长度不一致，以最长为标准填"N/A"字符串,如果有null也替换
            int maxLength = 0;
            for (int i = 0; i < mTableDatas.size(); i++) {
                if (mTableDatas.get(i).size() >= maxLength) {
                    maxLength = mTableDatas.get(i).size();
                }
                ArrayList<String> rowDatas = mTableDatas.get(i);
                for (int j = 0; j < rowDatas.size(); j++) {
                    if (rowDatas.get(j) == null || rowDatas.get(j).equals("")) {
                        rowDatas.set(j, mNullableString);
                    }
                }
                mTableDatas.set(i, rowDatas);
            }
            for (int i = 0; i < mTableDatas.size(); i++) {
                ArrayList<String> rowDatas = mTableDatas.get(i);
                if (rowDatas.size() < maxLength) {
                    for (int j = 0; j < maxLength - rowDatas.size(); j++) {
                        rowDatas.add(mNullableString);
                    }
                    mTableDatas.set(i, rowDatas);
                }
            }
            //初始化每列最大宽度
            for (int i = 0; i < mTableDatas.size(); i++) {
                ArrayList<String> rowDatas = mTableDatas.get(i);
                StringBuffer buffer = new StringBuffer();
                for (int j = 0; j < rowDatas.size(); j++) {
                    TextView textView = new TextView(mContext);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextViewSize);
                    textView.setText(rowDatas.get(j));
                    textView.setGravity(Gravity.CENTER);
                    //设置布局
                    LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textViewParams.setMargins(45, 45, 45, 45);//android:layout_margin="15dp"
                    textView.setLayoutParams(textViewParams);
                    if (i == 0) {
                        mColumnMaxWidths.add(measureTextWidth(textView, rowDatas.get(j)));
                        buffer.append("[" + measureTextWidth(textView, rowDatas.get(j)) + "]");
                    } else {
                        int length = mColumnMaxWidths.get(j);
                        int current = measureTextWidth(textView, rowDatas.get(j));
                        if (current > length) {
                            mColumnMaxWidths.set(j, current);
                        }
                        buffer.append("[" + measureTextWidth(textView, rowDatas.get(j)) + "]");
                    }
                }
//                Log.e("第"+i+"行列最大宽度",buffer.toString());
            }
//            Log.e("每列最大宽度",mColumnMaxWidths.toString());

//            //测试
//            for (int i=0;i<mTableDatas.size();i++){
//                ArrayList<String> rowDatas=mTableDatas.get(i);
//                StringBuffer b=new StringBuffer();
//                for (String str:rowDatas){
//                    b.append("["+str+"]");
//                }
//                Log.e("第"+i+"行数据",b.toString());
//            }

            if (isLockFristRow) {
                ArrayList<String> fristRowDatas = mTableDatas.get(0);
                if (isLockFristColumn) {
                    //锁定第一列
                    mColumnTitle = fristRowDatas.get(0);
                    fristRowDatas.remove(0);
                    mTableFristData.addAll(fristRowDatas);
                    //构造第一列数据,并且构造表格每行数据
                    for (int i = 1; i < mTableDatas.size(); i++) {
                        ArrayList<String> rowDatas = mTableDatas.get(i);
                        mTableColumnDatas.add(rowDatas.get(0));
                        rowDatas.remove(0);
                        mTableRowDatas.add(rowDatas);
                    }
                } else {
                    mTableFristData.addAll(fristRowDatas);
                    for (int i = 1; i < mTableDatas.size(); i++) {
                        mTableRowDatas.add(mTableDatas.get(i));
                    }
                }
            } else {
                if (isLockFristColumn) {
                    //锁定第一列
                    //构造第一列数据,并且构造表格每行数据
                    for (int i = 0; i < mTableDatas.size(); i++) {
                        ArrayList<String> rowDatas = mTableDatas.get(i);
                        mTableColumnDatas.add(rowDatas.get(0));
                        rowDatas.remove(0);
                        mTableRowDatas.add(rowDatas);
                    }
                } else {
                    for (int i = 0; i < mTableDatas.size(); i++) {
                        mTableRowDatas.add(mTableDatas.get(i));
                    }
                }
            }

//            Log.e("第一行数据",mTableFristData.toString());
//            Log.e("第一列数据",mTableColumnDatas.toString());
//            Log.e("每行数据",mTableRowDatas.toString());
        } else {
            Toast.makeText(mContext, "表格数据为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化表格视图
     */
    private void initView() {
        mColumnTitleView = (TextView) mTableView.findViewById(R.id.lockHeadView_Text);
        mLockHeadView = (LinearLayout) mTableView.findViewById(R.id.lockHeadView);
        mUnLockHeadView = (LinearLayout) mTableView.findViewById(R.id.unLockHeadView);
        mLockScrollView = (CustomHorizontalScrollView) mTableView.findViewById(R.id.lockHeadView_ScrollView);
        mUnLockScrollView = (CustomHorizontalScrollView) mTableView.findViewById(R.id.unlockHeadView_ScrollView);
        mTableScrollView = (ScrollView) mTableView.findViewById(R.id.table_scrollView);
        mLockHeadView.setBackgroundColor(ContextCompat.getColor(mContext, mFristRowBackGroudColor));
        mUnLockHeadView.setBackgroundColor(ContextCompat.getColor(mContext, mFristRowBackGroudColor));
        if (isLockFristRow) {
            if (isLockFristColumn) {
                mLockHeadView.setVisibility(View.VISIBLE);
                mUnLockHeadView.setVisibility(View.GONE);
            } else {
                mLockHeadView.setVisibility(View.GONE);
                mUnLockHeadView.setVisibility(View.VISIBLE);
            }
            creatHeadView();
        } else {
            mLockHeadView.setVisibility(View.GONE);
            mUnLockHeadView.setVisibility(View.GONE);
        }
        createTableView();
    }

    /**
     * 创建头部视图
     */
    private void creatHeadView() {
        if (isLockFristColumn) {
            mColumnTitleView.setTextColor(ContextCompat.getColor(mContext, mTableHeadTextColor));
            mColumnTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextViewSize);
            mColumnTitleView.setText(mColumnTitle);
            ViewGroup.LayoutParams layoutParams = mColumnTitleView.getLayoutParams();
            layoutParams.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(0));
            mColumnTitleView.setLayoutParams(layoutParams);
            //构造滚动视图
            createScollview(mLockScrollView, mTableFristData, true);
            mScrollViews.add(mLockScrollView);
            mLockScrollView.setOnScrollChangeListener(new CustomHorizontalScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    changeAllScrollView(x, y);
                }
            });
        } else {
            createScollview(mUnLockScrollView, mTableFristData, true);
            mScrollViews.add(mUnLockScrollView);
            mUnLockScrollView.setOnScrollChangeListener(new CustomHorizontalScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y) {
                    changeAllScrollView(x, y);
                }
            });
        }
    }

    /**
     * 构造表格主视图
     */
    private void createTableView() {
        if (isLockFristColumn) {
            createLockColumnView();
        } else {
            createUnLockColumnView();
        }
    }

    /**
     * 创建锁定列视图
     */
    private void createLockColumnView() {
        //构造ScrollView子视图
        LinearLayout linearLayout=new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        for (int i=0;i<mTableRowDatas.size();i++){
            View tableItemView=LayoutInflater.from(mContext).inflate(R.layout.locktable_item,null);
            LinearLayout contentView=(LinearLayout)tableItemView.findViewById(R.id.lockView);
            TextView lockView=(TextView)tableItemView.findViewById(R.id.lockView_Text);
            CustomHorizontalScrollView scrollView=(CustomHorizontalScrollView)tableItemView.findViewById(R.id.lockView_ScrollView);
            if(!isLockFristRow){
                if (i==0){
                    lockView.setTextColor(ContextCompat.getColor(mContext, mTableHeadTextColor));
                    contentView.setBackgroundColor(ContextCompat.getColor(mContext, mFristRowBackGroudColor));
                    createScollview(scrollView , mTableRowDatas.get(i), true);
                }else{
                    lockView.setTextColor(ContextCompat.getColor(mContext, mTableContentTextColor));
                    createScollview(scrollView , mTableRowDatas.get(i), false);
                }
            }else{
                createScollview(scrollView , mTableRowDatas.get(i), false);
            }
            //加入滚动视图列表，并设置滚动监听
            mScrollViews.add(scrollView );
            scrollView .setOnScrollChangeListener(new CustomHorizontalScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y){
                    changeAllScrollView(x, y);
                }
            });
            lockView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextViewSize);
            lockView.setText(mTableColumnDatas.get(i));
            ViewGroup.LayoutParams lockViewLayoutParams = lockView.getLayoutParams();
            lockViewLayoutParams.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(0));
            lockView.setLayoutParams(lockViewLayoutParams);
            linearLayout.addView(tableItemView);
        }
        //加入子视图
        mTableScrollView.addView(linearLayout);
    }

    /**
     * 创建不锁定列视图
     */
    private void createUnLockColumnView() {
        //构造ScrollView子视图
        LinearLayout linearLayout=new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        for (int i=0;i<mTableRowDatas.size();i++){
            View tableItemView=LayoutInflater.from(mContext).inflate(R.layout.unlocktable_item,null);
            LinearLayout contentView=(LinearLayout)tableItemView.findViewById(R.id.lockView);
            CustomHorizontalScrollView scrollView=(CustomHorizontalScrollView)tableItemView.findViewById(R.id.lockView_ScrollView);
            if(!isLockFristRow){
                if (i==0){
                    contentView.setBackgroundColor(ContextCompat.getColor(mContext, mFristRowBackGroudColor));
                    createScollview(scrollView , mTableRowDatas.get(i), true);
                }else{
                    createScollview(scrollView , mTableRowDatas.get(i), false);
                }
            }else{
                createScollview(scrollView , mTableRowDatas.get(i), false);
            }
            //加入滚动视图列表，并设置滚动监听
            mScrollViews.add(scrollView );
            scrollView .setOnScrollChangeListener(new CustomHorizontalScrollView.onScrollChangeListener() {
                @Override
                public void onScrollChanged(HorizontalScrollView scrollView, int x, int y){
                    changeAllScrollView(x, y);
                }
            });
            linearLayout.addView(tableItemView);
        }
        //加入子视图
        mTableScrollView.addView(linearLayout);
    }

    /**
     * 改变所有滚动视图位置
     *
     * @param x
     * @param y
     */
    private void changeAllScrollView(int x, int y) {
        if (mScrollViews.size() > 0) {
            for (int i = 0; i < mScrollViews.size(); i++) {
                HorizontalScrollView scrollView = mScrollViews.get(i);
                scrollView.scrollTo(x, y);
            }
        }
    }

    /**
     * 根据最大最小值，计算TextView的宽度
     *
     * @param textView
     * @param text
     * @return
     */
    private int measureTextWidth(TextView textView, String text) {
        if (textView != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            int width = DisplayUtil.px2dip(mContext, layoutParams.leftMargin) +
                    DisplayUtil.px2dip(mContext, layoutParams.rightMargin) +
                    getTextViewWidth(textView, text);
            if (width <= minColumnWidth) {
                return minColumnWidth;
            } else if (width > minColumnWidth && width <= maxColumnWidth) {
                return width;
            } else {
                return maxColumnWidth;
            }
        }
        return 0;
    }

    /**
     * 根据文字计算textview的宽度
     *
     * @param view
     * @param text
     * @return
     */
    private int getTextViewWidth(TextView view, String text) {
        if (view != null) {
            TextPaint paint = view.getPaint();
            return DisplayUtil.px2dip(mContext, (int) paint.measureText(text));
        }
        return 0;
    }


    /**
     * 构造滚动视图
     *
     * @param scrollView
     * @param datas
     * @param isFristRow 是否是第一行
     */
    private void createScollview(HorizontalScrollView scrollView, List<String> datas, boolean isFristRow) {
        //设置LinearLayout
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
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
            textViewParams.setMargins(45, 45, 45, 45);
            textView.setLayoutParams(textViewParams);
            ViewGroup.LayoutParams textViewParamsCopy = textView.getLayoutParams();
            if (isLockFristColumn) {
                textViewParamsCopy.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(i + 1));
            } else {
                textViewParamsCopy.width = DisplayUtil.dip2px(mContext, mColumnMaxWidths.get(i));
            }
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
        scrollView.addView(linearLayout);
    }


    //属性设置
    public LockTableView setLockFristRow(boolean lockFristRow) {
        isLockFristRow = lockFristRow;
        return this;
    }

    public LockTableView setLockFristColumn(boolean lockFristColumn) {
        isLockFristColumn = lockFristColumn;
        return this;
    }

    public LockTableView setMaxColumnWidth(int maxColumnWidth) {
        this.maxColumnWidth = maxColumnWidth;
        return this;
    }

    public LockTableView setMinColumnWidth(int minColumnWidth) {
        this.minColumnWidth = minColumnWidth;
        return this;
    }

    public LockTableView setFristRowBackGroudColor(int mFristRowBackGroudColor) {
        this.mFristRowBackGroudColor = mFristRowBackGroudColor;
        return this;
    }

    public LockTableView setNullableString(String mNullableString) {
        this.mNullableString = mNullableString;
        return this;
    }

    public LockTableView setTextViewSize(int mTextViewSize) {
        this.mTextViewSize = mTextViewSize;
        return this;
    }

    public LockTableView setTableHeadTextColor(int mTableHeadTextColor) {
        this.mTableHeadTextColor = mTableHeadTextColor;
        return this;
    }

    public LockTableView setTableContentTextColor(int mTableContentTextColor) {
        this.mTableContentTextColor = mTableContentTextColor;
        return this;
    }
}
