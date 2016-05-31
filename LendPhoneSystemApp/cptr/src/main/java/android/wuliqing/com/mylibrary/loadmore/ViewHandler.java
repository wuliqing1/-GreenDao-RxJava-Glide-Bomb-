package android.wuliqing.com.mylibrary.loadmore;

import android.view.View;
import android.view.View.OnClickListener;


public interface ViewHandler {

	public boolean handleSetAdapter(View contentView, ILoadViewMoreFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);

	public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener);

}
