package com.rmondjone.locktableviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.rmondjone.locktableview.LockTableView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentView=(LinearLayout)findViewById(R.id.contentView);

        //构造假数据
        ArrayList<ArrayList<String>> mTableDatas=new ArrayList<ArrayList<String>>();
        ArrayList<String> mfristData=new ArrayList<String>();
//        mfristData.add("标题");
        for (int i=0;i<2;i++){
            mfristData.add("数据111111111111"+i);
        }
        mTableDatas.add(mfristData);
        for (int i=0;i<22;i++){
            ArrayList<String> mRowDatas=new ArrayList<String>();
            mRowDatas.add("标题"+i);
            mRowDatas.add("数据11111111111"+i);
            for (int j=0;j<10;j++){
                mRowDatas.add("数据"+j);
            }
            mTableDatas.add(mRowDatas);
        }
        LockTableView mLockTableView=new LockTableView(this,mContentView,mTableDatas);
        mLockTableView.setLockFristColumn(true) //是否锁定第一列
                .setLockFristRow(true) //是否锁定第一行
                .setMaxColumnWidth(100) //列最大宽度
                .setMinColumnWidth(70) //列最小宽度
                .setMinRowHeight(20)//行最小高度
                .setMaxRowHeight(60)//行最大高度
                .setTextViewSize(16) //单元格字体大小
                .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                .setTableContentTextColor(R.color.border_color)//单元格字体颜色
                .setNullableString("N/A") //空值替换值
                .show(); //显示表格,此方法必须调用
    }
}
