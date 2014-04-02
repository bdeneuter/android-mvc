package be.cegeka.android_mvc;

import rx.Observable;
import rx.functions.Func2;

public class Properties {

    public static <T1, T2, R> Observable<R> combineLatest(Property<? extends T1> o1, Property<? extends T2> o2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        return Observable.combineLatest(o1.asObservable(), o2.asObservable(), combineFunction);
    }
}
