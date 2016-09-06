package android.wuliqing.com.myapplication.Rx;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by 10172915 on 2016/7/29.
 */
public class RxJavaOperators {
    public static final String TAG = "RxJavaOperators";

    /**
     * 创建操作符
     * 最基础的创建操作符就是create()操作符,同时create()操作符也是所有创建操作符的根，
     * 也就是说其他的创建操作符最后都是调用create操作符来创建Observable的
     */
    public void createOperator() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("observable");
                subscriber.onCompleted();
            }
        });
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, (String) o);
            }
        };
        observable.subscribe(subscriber);
    }

    /**
     * 创建操作符
     * From操作符用来将某个对象转化为Observable对象，并且依次将其内容发射出去。
     */
    public void fromOperator() {
        String[] words = {"observable", "observable2"};
        Observable.from(words)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, s + "");
                    }
                });
    }

    /**
     * 创建操作符
     * Just操作符将某个对象转化为Observable对象，并且将其发射出去，可以使一个数字、一个字符串、数组、Iterate对象等
     */
    public void justOperator() {
        Observable.just(1, 2, 3, 4)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * Defer操作符只有当有Subscriber来订阅的时候才会创建一个新的Observable对象,
     * 也就是说每次订阅都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
     */
    public void deferOperator() {
        Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(1, 2, 3, 4, 5);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, integer + "");
            }
        });
    }

    /**
     * 创建操作符
     * Range操作符根据出入的初始值n和数目m发射一系列大于等于n的m个值
     */
    public void rangeOperator() {
        Observable.range(10, 5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 创建操作符
     * Interval所创建的Observable对象会从0开始，每隔固定的时间发射一个数字。
     * 需要注意的是这个对象是运行在computation Scheduler,所以如果需要在view中显示结果，要在主线程中订阅。
     */
    public void intervalOperator() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, aLong + "");
                    }
                });
    }

    /**
     * 创建操作符
     * Repeat会将一个Observable对象重复发射，我们可以指定其发射的次数
     */
    public void repeatOperator() {
        Observable.just(1).repeat(5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 创建操作符
     * Timer会在指定时间后发射一个数字0，注意其也是运行在computation Scheduler
     */
    public void timerOperator() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(TAG, aLong + "");
                    }
                });
    }

    /**
     * 转换操作符
     * buffer操作符周期性的收集源Observable产生的结果到列表中，并把这个列表提交给订阅者，
     * 订阅者处理后，清空buffer列表，同时接收下一次收集的结果提交给订阅者，周而复始。
     * buffer操作符有两个参数，分别为count和skip，count参数指定buffer操作符的大小，skip参数用来指定每次发射一个集合需要跳过几个数
     */
    public void bufferOperator() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8).buffer(3, 2)
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        Log.d(TAG, integers + "");
                    }
                });
    }

    /**
     * 转换操作符
     * flatMap操作符是把Observable产生的结果转换成多个Observable，
     * 然后把这多个Observable"扁平化"成一个Observable，并依次提交产生的结果给订阅者
     */
    public void flatMapOperator() {
        Observable.just(1, 2, 3, 4).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(1 + integer);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                Log.d(TAG, s + "");
            }
        });
    }

    /**
     * 转换操作符
     * GroupBy操作符是对源Observable产生的结果进行分组，形成一个类型为GroupedObservable的结果集，
     * GroupedObservable中存在一个方法为getKey(),可以通过该方法获取结果集的Key值。
     */
    public void groupByOperator() {
        Observable.interval(1, TimeUnit.SECONDS).take(10).groupBy(new Func1<Long, Long>() {
            @Override
            public Long call(Long aLong) {
                return aLong % 4;
            }
        }).subscribe(new Action1<GroupedObservable<Long, Long>>() {
            @Override
            public void call(final GroupedObservable<Long, Long> result) {
                result.subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d(TAG, "key:" + result.getKey() + ",value:" + aLong);
                    }
                });
            }
        });
    }

    /**
     * 转换操作符
     * Map操作符的功能类似于FlatMap，不同之处在于它对数据的转化是直接进行的，而FlatMap需要通过一些中间的Observables来进行。
     */
    public void mapOperator() {
        Observable.just(1, 2, 3, 4).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer + 1;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, integer + "");
            }
        });
    }

    /**
     * 转换操作符
     * Scan操作符对一个序列的数据应用一个函数，并将这个函数的结果发射出去作为下个数据应用这个函数时候的第一个参数使用，有点类似于递归操作
     */
    public void scanOperator() {
        Observable.just(1, 2, 3, 4, 5, 6).scan(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, integer + "");
            }
        });
    }

    /**
     * 转换操作符
     * window操作符与buffer操作符类似，区别在于buffer操作符产生的结果是一个List缓存，
     * 而window操作符产生的结果是一个Observable,订阅者可以对这个结果Observable重新进行订阅处理。
     */
    public void windowOperator() {
        Observable.interval(1, TimeUnit.SECONDS).take(12)
                .window(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Observable<Long>>() {
                    @Override
                    public void call(Observable<Long> longObservable) {
                        Log.d(TAG, "start");
                        longObservable.subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Log.d(TAG, aLong + "");
                            }
                        });
                    }
                });
    }

    /**
     * 过滤操作符
     * debounce操作符对源Observable产生一个结果后，如果在规定的间隔时间内没有别的结果产生，则把这个结果提交给订阅者处理，否则忽略该结果。
     */
    public void debounceOperator() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 1; i < 10; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(i * 100);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "omCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * Distinct操作符的用处就是用来去重，会对源Observable产生的结果进行过滤，把重复的结果过滤掉，只输出不重复的结果给订阅者
     */
    public void DistinctOperator() {
        Observable.just(1, 2, 3, 1, 2, 2, 4)
                .distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * ElementAt只会返回指定位置的数据，注意索引的值从0开始的
     */
    public void elementAtOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .elementAt(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * fliter操作符是对源Observable产生的结果按照指定的条件进行过滤
     */
    public void filterOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return (integer > 3);
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, integer + "");
            }
        });
    }

    /**
     * 过滤操作符
     * ofType操作符按照类型进行过滤
     */
    public void ofTypeOperator() {
        Observable.just(1, "alla", 1.0, true)
                .ofType(String.class)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, s);
                    }
                });
    }

    /**
     * 过滤操作符
     * First操作符只会返回第一条数据，并且还可以返回满足条件的第一条数据
     */
    public void firstOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .first()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * last操作符只会返回最后一条数据，并且还可以返回满足条件的最后一条数据
     */
    public void lastOperator() {
        Observable.just(1, 2, 3, 4, 5)
                .last()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * Skip操作符将源Observable发射的数据过滤掉前n项
     */
    public void skipOperator() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .skip(3)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * Take操作符则只取前n项
     */
    public void takeOperator() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .take(4)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }

    /**
     * 过滤操作符
     * sample操作符定期扫描源Observable产生的结果，在指定的时间间隔范围内对源Observable产生的结果进行采样
     */
    public void sampleOperator() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i = 1; i < 9; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(1000);
                    }
                    Thread.sleep(2000);
                    subscriber.onNext(9);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .sample(2200, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "omCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error" + e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, integer + "");
                    }
                });
    }
}
