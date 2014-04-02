package be.cegeka.android_mvc.binder;

import java.util.List;

import rx.Subscription;

import static com.google.common.collect.Lists.newArrayList;

public class Subscriptions {

    private List<Subscription> subscriptions = newArrayList();

    public void add(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void unsubscribe() {
        for(Subscription subscription: subscriptions) {
            subscription.unsubscribe();
        }
        subscriptions.clear();
    }
}
