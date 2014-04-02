package be.cegeka.demo.demo;

import be.cegeka.android_mvc.PresentationModel;
import be.cegeka.android_mvc.Property;
import be.cegeka.android_mvc.binder.Bind;
import rx.Observable;
import rx.functions.Func2;

import static be.cegeka.android_mvc.Properties.combineLatest;
import static be.cegeka.android_mvc.Property.newProperty;
import static be.cegeka.android_mvc.binder.Visibility.GONE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MainModel implements PresentationModel {

    @Bind(R.id.name)
    Property<String> name = newProperty("Bart");

    @Bind(R.id.password)
    Property<String> password = newProperty("Hello");

    @Bind(value = R.id.warning, visibilityToUse = GONE)
    Observable<Boolean> invalid;

    @Override
    public void onCreate() {
        invalid = combineLatest(name, password, new Func2<String, String, Boolean>() {
            @Override
            public Boolean call(String name, String password) {
                return !validate(name, password);
            }
        });
    }

    private boolean validate(String name, String password) {
        return isNotBlank(name) && isNotBlank(password);
    }

}
