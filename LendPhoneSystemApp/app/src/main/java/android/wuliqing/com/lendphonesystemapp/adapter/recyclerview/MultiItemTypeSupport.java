package android.wuliqing.com.lendphonesystemapp.adapter.recyclerview;

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}