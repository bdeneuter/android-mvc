package be.cegeka.android_mvc;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import be.cegeka.android_mvc.binder.Subscriptions;

import static be.cegeka.android_mvc.binder.Binder.bindView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class Fragment extends android.app.Fragment {

    private PresentationModel model;
    private Subscriptions subscriptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = createModel();
        model.onCreate();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscriptions = bindView(this, model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriptions.unsubscribe();
    }

    public abstract PresentationModel createModel();

}
