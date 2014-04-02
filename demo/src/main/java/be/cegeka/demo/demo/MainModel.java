package be.cegeka.demo.demo;

import be.cegeka.android_mvc.binder.Bind;
import be.cegeka.android_mvc.binder.Property;
import rx.Observable;
import rx.functions.Func2;

import static be.cegeka.android_mvc.binder.Visibility.GONE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static rx.Observable.combineLatest;

public class MainModel {

    @Bind(R.id.name)
    Property<String> name = Property.create("Bart");

    @Bind(R.id.password)
    Property<String> password = Property.create("Hello");

    @Bind(value = R.id.warning, visibilityToUse = GONE)
    Observable<Boolean> valid;

    MainModel() {
        valid = combineLatest(name.asObservable(), password.asObservable(), new Func2<String, String, Boolean>() {
            @Override
            public Boolean call(String name, String password) {
                return validate(name, password);
            }
        });
    }

    private boolean validate(String name, String password) {
        return isNotBlank(name) && isNotBlank(password);
    }

}
