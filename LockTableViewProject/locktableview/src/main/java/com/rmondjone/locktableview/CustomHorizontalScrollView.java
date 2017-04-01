package com.rmondjone.locktableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 说明 自定义水平滚动视图，解决ScrollView在API23以下没有滚动监听事件问题
 * 作者 郭翰林
 * 创建时间 2017/3/31.
 */

public class CustomHorizontalScrollView extends HorizontalScrollView{
    private onScrollChangeListener onScrollChangeListener;

    public CustomHorizontalScrollView(Context context) {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface onScrollChangeListener{
        void onScrollChanged(HorizontalScrollView scrollView,int x,int y);
    }

    /**
     * 设置监听
     * @param onScrollChangeListener
     */
    public void setOnScrollChangeListener(CustomHorizontalScrollView.onScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //回调
        if (onScrollChangeListener!=null){
            onScrollChangeListener.onScrollChanged(this,l,t);
        }
    }

}
