package be.cegeka.android_mvc.binder;

import rx.Observer;

public abstract class DefaultObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

}
