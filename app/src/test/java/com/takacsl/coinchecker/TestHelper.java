package com.takacsl.coinchecker;


import org.robolectric.shadows.ShadowLog;

import androidx.test.core.app.ApplicationProvider;

public class TestHelper {

    public static DaggerTestComponent setTestInjector() {
        ShadowLog.stream = System.out;
        CoinCheckerApplication application = ApplicationProvider.getApplicationContext();
        CoinCheckerApplicationComponent injector = DaggerTestComponent.builder().testModule(new TestModule(application.getApplicationContext())).build();
        application.injector = injector;
        return (DaggerTestComponent) injector;
    }
}
