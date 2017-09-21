# LockTableView
自定义表格,可锁定双向表头,自适应列宽,自适应行高,快速集成<br>
## 效果展示

![image](https://github.com/RmondJone/LockTableView/blob/master/show.gif)

## 更新日志
* 2017年04月01日13:02:01----------LockTableView V1.0.0

* 2017年04月01日17:33:16----------LockTableView V1.0.1

  BUG修改，数据过多时会引起堆栈内存溢出，重新设计了一下，已经解决

* 2017年04月03日11:46:53----------LockTableView V1.0.2

  BUG修改，缺省值BUG修改，自适应行高属性新增

* 2017年04月03日14:55:00----------LockTableView V1.0.3

  BUG修改，在低分辨率设备表头会错位的问题

  新增滚动回调监听事件,新增一些属性值获取,方便开发者自己拓展控件功能

* 2017年06月30日16:23:27----------LockTableView V1.0.4

  BUG修改，如果少量数据时，重复更新数据，会重建表格问题。

* 2017年09月18日22:02:57----------LockTableView V1.0.5

  版本重构：1、当数据量过大引起的OOM问题。2、新增下拉刷新上拉加载功能

* 2017年09月21日21:34:32----------LockTableView V1.0.6

  BUG修改：低分辨率表格错位问题

## 工程集成说明
* 第一步
```java
//在工程gradle文件里
allprojects {
    repositories {
        .......
        maven { url 'https://jitpack.io' }
        ......
    }
}
```

```java
//如果不在工程gradle文件里加入，也可以加入模块gradle文件中
repositories {
    maven {
        url  "https://jitpack.io"
    }
}
```

* 第二步
```java
  dependencies {
		compile 'com.github.RmondJone:LockTableView:1.0.6'
	}
```

## API使用说明

```java

final LockTableView mLockTableView = new LockTableView(this, mContentView, mTableDatas);
Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
mLockTableView.setLockFristColumn(true) //是否锁定第一列
      .setLockFristRow(true) //是否锁定第一行
      .setMaxColumnWidth(100) //列最大宽度
      .setMinColumnWidth(60) //列最小宽度
      .setMinRowHeight(20)//行最小高度
      .setMaxRowHeight(60)//行最大高度
      .setTextViewSize(16) //单元格字体大小
      .setFristRowBackGroudColor(R.color.table_head)//表头背景色
      .setTableHeadTextColor(R.color.beijin)//表头字体颜色
      .setTableContentTextColor(R.color.border_color)//单元格字体颜色
      .setNullableString("N/A") //空值替换值
      .setTableViewListener(new LockTableView.OnTableViewListener() {//设置横向滚动监听
          @Override
          public void onTableViewScrollChange(int x, int y) {
              Log.e("滚动值","["+x+"]"+"["+y+"]");
          }
      })//设置滚动回调监听
      .setOnLoadingListener(new LockTableView.OnLoadingListener() {//下拉刷新、上拉加载监听
          @Override
          public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      Log.e("现有表格数据", mTableDatas.toString());
                      //构造假数据
                      ArrayList<ArrayList<String>> mTableDatas = new ArrayList<ArrayList<String>>();
                      ArrayList<String> mfristData = new ArrayList<String>();
                      mfristData.add("标题");
                      for (int i = 0; i < 4; i++) {
                          mfristData.add("标题" + i);
                      }
                      mTableDatas.add(mfristData);
                      for (int i = 0; i < 20; i++) {
                          ArrayList<String> mRowDatas = new ArrayList<String>();
                          mRowDatas.add("标题" + i);
                          for (int j = 0; j < 4; j++) {
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

/**
 * 构造方法
 *
 * @param mContext     上下文
 * @param mContentView 表格父视图
 * @param mTableDatas  表格数据
 */
public LockTableView(Context mContext, ViewGroup mContentView, ArrayList<ArrayList<String>> mTableDatas) {
    this.mContext = mContext;
    this.mContentView = mContentView;
    this.mTableDatas = mTableDatas;
    initAttrs();
}

```
## 目前支持可自定义属性

```java
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
 * 最大行高(dp)
 */
private int maxRowHeight;
/**
  * 最小行高dp)
  */
private int minRowHeight;
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
/**
 * 表格监听事件
 */
private OnTableViewListener mTableViewListener;
/**
 * 表格上拉刷新、下拉加载监听事件
 */
private OnLoadingListener mOnLoadingListener;

```

## 问题反馈
* 技术交流群：QQ(264587303)
* Demo作者：郭翰林
