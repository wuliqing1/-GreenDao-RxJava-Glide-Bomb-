package android.wuliqing.com.myapplication.Gank;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 10172915 on 2016/7/29.
 */
public interface RxGankService {
    @GET("all/20/{page}")
    Observable<GankResultBean> getAndroidData(@Path("page") int page);
}
