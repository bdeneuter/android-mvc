package be.cegeka.android_mvc;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

public class Property<T> implements Observer<T> {

    private BehaviorSubject<T> delegate;

    private Property(BehaviorSubject<T> delegate) {
        this.delegate = delegate;
    }

    public Subscription subscribe(Observer<T> observer) {
        return delegate.subscribe(observer);
    }

    public Property<T> subscribeOn(Scheduler scheduler) {
        delegate.subscribeOn(scheduler);
        return this;
    }

    public Property<T> observeOn(Scheduler scheduler) {
        delegate.observeOn(scheduler);
        return this;
    }

    public Observable<T> asObservable() {
        return delegate;
    }

    public void onCompleted() {
        delegate.onCompleted();
    }

    public void onError(Throwable e) {
        delegate.onError(e);
    }

    public void onNext(T value) {
        delegate.onNext(value);
    }

    public static <T> Property<T> create() {
        return new Property<T>(BehaviorSubject.create((T) null));
    }

    public static <T> Property<T> newProperty(T value) {
        return new Property<T>(BehaviorSubject.create(value));
    }

}
