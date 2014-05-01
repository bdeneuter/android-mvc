package be.cegeka.android_mvc.binder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.Observable;

import be.cegeka.android_mvc.Property;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.BooleanSubscription;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class EditTextStrategy implements BindStrategy {

    @Override
    public boolean canBind(View view, Object field, Bind annotation) {
        return view instanceof EditText;
    }

    public void bind(View view, Object field, Bind annotation, Subscriptions subscriptions) {
        bind((EditText) view, field, subscriptions);
    }

    public void bind(EditText view, Object field, Subscriptions subscriptions) {
        if (field instanceof Property) {
            bind(view, (Property<String>) field, subscriptions);
        } else if (field instanceof Observable) {
            bind(view, (rx.Observable<String>) field, subscriptions);
        }
    }

    private static void bind(final EditText view, final rx.Observable<String> field, Subscriptions subscriptions) {
        subscriptions.add(
                field.observeOn(mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String value) {
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
