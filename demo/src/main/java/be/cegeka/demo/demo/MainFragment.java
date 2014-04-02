package be.cegeka.demo.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.cegeka.android_mvc.binder.Binder;
import be.cegeka.android_mvc.binder.Subscriptions;

public class MainFragment extends Fragment {

    private MainModel model;
    private Subscriptions subscriptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        model = new MainModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscriptions = Binder.bind(this, model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriptions.unsubscribe();
    }
}
