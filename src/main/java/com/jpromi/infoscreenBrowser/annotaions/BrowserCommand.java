package com.jpromi.infoscreenBrowser.annotaions;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BrowserCommand {
    String value();
}
