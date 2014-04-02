package be.cegeka.android_mvc.binder;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Observable;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.BooleanSubscription;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class Binder {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Subscriptions bind(Fragment fragment, Object model) {
        try {
            return bind(fragment.getView(), model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Subscriptions bind(android.support.v4.app.Fragment fragment, Object model) {
        try {
            return bind(fragment.getView(), model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Subscriptions bind(View parent, Object model) throws IllegalAccessException {
        Subscriptions subscriptions = new Subscriptions();
        for (Field field : model.getClass().getDeclaredFields()) {
            Bind annotation = field.getAnnotation(Bind.class);
            if (annotation != null) {
                bind(annotation, field, parent, model, subscriptions);
            }
        }
        return subscriptions;
    }

    private static void bind(Bind annotation, Field field, View parent, Object model, Subscriptions subscriptions) throws IllegalAccessException {
        View view = parent.findViewById(annotation.value());
        if (view == null) {
            throw new IllegalArgumentException("View with id " + annotation.value() + " is not found");
        }

        field.setAccessible(true);
        if (annotation.visibilityToUse() != Visibility.NULL) {
            bind(view, field.get(model), annotation.visibilityToUse(), subscriptions);
        } else if (view instanceof EditText) {
            bind((EditText) view, field.get(model), subscriptions);
        } else {
            throw new RuntimeException("Binding not yet implemented for " + view.getClass());
        }

    }

    private static void bind(final View view, Object field, final Visibility visibility, Subscriptions subscriptions) {
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
                } else if (visibility == Visibility.GONE) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }));
    }

    private static void bind(EditText view, Object field, Subscriptions subscriptions) {
        if (field instanceof Property) {
            bind(view, (Property<String>) field, subscriptions);
        } else if (field instanceof Observable) {
            bind(view, (rx.Observable<String>) field, subscriptions);
        }
    }

    private static void bind(final EditText view, final rx.Observable<String> field, Subscriptions subscriptions) {
        subscriptions.add(
                field.observeOn(mainThread())
                        .subscribe(new DefaultObserver<String>() {
                            @Override
                            public void onNext(String value) {
                                if ((view.getText() == null && value != null) || !view.getText().toString().equals(value)) {
                                    view.setText(value);
                                }
                            }
                        }));
    }

    private static void bind(final EditText view, final Property<String> field, Subscriptions subscriptions) {
        bind(view, field.asObservable(), subscriptions);

        final TextWatcher listener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                field.onNext(s.toString());
            }
        };
        view.addTextChangedListener(listener);
        subscriptions.add(BooleanSubscription.create(new Action0() {
            @Override
            public void call() {
                view.removeTextChangedListener(listener);
            }
        }));
    }

}
