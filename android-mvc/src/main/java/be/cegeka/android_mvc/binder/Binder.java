package be.cegeka.android_mvc.binder;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Binder {

    private static final Binder BINDER = new Binder();

    private List<BindStrategy> strategies = new ArrayList<>();

    private Binder() {
        strategies.add(new VisibleBindStrategy());
        strategies.add(new EditTextStrategy());
    }

    public static void addBindStrategy(BindStrategy bindStrategy) {
        BINDER.strategies.add(bindStrategy);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Subscriptions bindView(Fragment fragment, Object model) {
        try {
            return BINDER.bind(fragment.getView(), model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Subscriptions bind(android.support.v4.app.Fragment fragment, Object model) {
        try {
            return BINDER.bind(fragment.getView(), model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Subscriptions bind(View parent, Object model) throws IllegalAccessException {
        Subscriptions subscriptions = new Subscriptions();
        for (Field field : model.getClass().getDeclaredFields()) {
            Bind annotation = field.getAnnotation(Bind.class);
            if (annotation != null) {
                bind(annotation, field, parent, model, subscriptions);
            }
        }
        return subscriptions;
    }

    private void bind(Bind annotation, Field field, View parent, Object model, Subscriptions subscriptions) throws IllegalAccessException {
        View view = parent.findViewById(annotation.value());
        if (view == null) {
            throw new IllegalArgumentException("View with id " + annotation.value() + " is not found");
        }

        field.setAccessible(true);
        Object fieldValue = field.get(model);
        BindStrategy strategy = findBindStrategy(view, fieldValue, annotation);
        strategy.bind(view, fieldValue, annotation, subscriptions);
    }

    private BindStrategy findBindStrategy(View view, Object field, Bind annotation) {
        for (BindStrategy strategy : strategies) {
            if (strategy.canBind(view, field, annotation)) {
                return strategy;
            }
        }
        throw new RuntimeException("Binding not yet implemented for " + view.getClass());
    }

}
