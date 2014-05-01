package be.cegeka.android_mvc.binder;

import android.view.View;

import be.cegeka.android_mvc.Property;
import rx.functions.Action1;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class VisibleBindStrategy implements BindStrategy {
    @Override
    public boolean canBind(View view, Object field, Bind annotation) {
        return annotation.visibilityToUse() != Visibility.NULL;
    }

    @Override
    public void bind(final View view, Object field, final Bind annotation, Subscriptions subscriptions) {
        rx.Observable<Boolean> observable;
        if (field instanceof Property) {
            observable = ((Property<Boolean>)field).asObservable();
        } else {
            observable = (rx.Observable<Boolean>) field;
        }
        subscriptions.add(observable.observeOn(mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean visible) {
                if (visible) {
                    view.setVisibility(View.VISIBLE);
                } else if (annotation.visibilityToUse() == Visibility.GONE) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }));
    }
}
