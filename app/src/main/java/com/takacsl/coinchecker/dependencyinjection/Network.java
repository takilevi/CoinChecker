package com.takacsl.coinchecker.dependencyinjection;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME) // not needed
public @interface Network {
}
