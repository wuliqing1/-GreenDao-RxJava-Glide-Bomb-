package android.wuliqing.com.myapplication.Rx;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by 10172915 on 2016/7/29.
 */
public class RxBusHelp {
    Subscription mSubscription;

    public void post(String msg) {
        RxBus.getInstance().post(msg);
    }

    public void subscribeBus() {
        mSubscription = RxBus.getInstance().toObserverable(String.class)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //handle rx msg
                    }
                });
    }

    public void unSubscribeBus() {
        mSubscription.unsubscribe();
    }
}
