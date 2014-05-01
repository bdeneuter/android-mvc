package be.cegeka.android_mvc.binder;

import android.view.View;

public interface BindStrategy {

    boolean canBind(View view, Object field, Bind annotation);

    void bind(View view, Object field, Bind annotation, Subscriptions subscriptions);

}
