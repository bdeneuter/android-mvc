package be.cegeka.android_mvc.binder;

import rx.Observable;
import rx.functions.Func2;

public class Properties {

    public <T> Observable<T> combineLatest(Property<?> p1, Property<?> p2) {
        return Observable.combineLatest(p1.asObservable(), p2.asObservable(), new Func2<Object, Object, T>() {
            @Override
            public T call(Object o, Object o2) {
                return null;
            }
        });
    }

}
