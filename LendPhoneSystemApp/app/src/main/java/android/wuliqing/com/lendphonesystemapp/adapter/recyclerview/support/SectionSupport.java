package android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.support;

/**
 * Created by zhy on 16/4/9.
 */
public interface SectionSupport<T> {
    int sectionHeaderLayoutId();

    int sectionTitleTextViewId();

    String getTitle(T t);
}
