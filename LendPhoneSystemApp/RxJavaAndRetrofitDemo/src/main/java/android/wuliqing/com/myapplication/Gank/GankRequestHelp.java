package android.wuliqing.com.myapplication.Gank;

import android.util.Log;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 10172915 on 2016/7/29.
 */
public class GankRequestHelp {
    public static void onRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RxGankService rxGankService = retrofit.create(RxGankService.class);
        final Observable<GankResultBean> observable = rxGankService.getAndroidData(1);
        observable.subscribeOn(Schedulers.io())//订阅发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//订阅者的回调在主线程
                .map(new Func1<GankResultBean, List<ResultBean>>() {//通过映射从GankResultBean获取用户需要的List<ResultBean>
                    @Override
                    public List<ResultBean> call(GankResultBean gankResultBean) {
                        return gankResultBean.getResults();
                    }
                })
                .flatMap(new Func1<List<ResultBean>, Observable<ResultBean>>() {//让结果一条一条发射出去
                    @Override
                    public Observable<ResultBean> call(List<ResultBean> resultBeen) {
                        return Observable.from(resultBeen);
                    }
                })
                .filter(new Func1<ResultBean, Boolean>() {//只接收Type为Android的数据
                    @Override
                    public Boolean call(ResultBean resultBean) {
                        return "Android".equals(resultBean.getType());
                    }
                })
                .subscribe(new Subscriber<ResultBean>() {
                    @Override
                    public void onNext(ResultBean resultBean) {
                        Log.d("wuliqing", resultBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }
}
