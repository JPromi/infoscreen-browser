package com.jpromi.infoscreenBrowser.annotaions;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BrowserClass {
    String value();
}
