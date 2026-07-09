package com.jpromi.infoscreenBrowser.registry;

import com.jpromi.infoscreenBrowser.annotaions.BrowserClass;
import com.jpromi.infoscreenBrowser.browser.BrowserApplicationContext;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;

public class BrowserCommandAutoLoader {

    public static void registerAll(
            BrowserCommandRegistry registry,
            BrowserApplicationContext context
    ) {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages("com.jpromi.infoscreenBrowser.commands")
                .scan()) {

            scanResult
                    .getClassesWithAnnotation(BrowserClass.class)
                    .forEach(classInfo -> {
                        try {
                            Class<?> clazz = classInfo.loadClass();

                            Object instance = createInstance(clazz, context);

                            registry.register(instance);

                        } catch (Exception e) {
                            throw new RuntimeException(
                                    "Could not register command class: " + classInfo.getName(),
                                    e
                            );
                        }
                    });
        }
    }

    private static Object createInstance(
            Class<?> clazz,
            BrowserApplicationContext context
    ) throws Exception {

        // Constructor mit Context
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(BrowserApplicationContext.class);
            constructor.setAccessible(true);
            return constructor.newInstance(context);
        } catch (NoSuchMethodException ignored) {
        }

        // Leerer Constructor
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}