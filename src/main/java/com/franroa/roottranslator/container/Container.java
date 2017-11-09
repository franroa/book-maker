package com.franroa.roottranslator.container;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Container {
    private static HashMap<Class,Object> bindings = new HashMap<>();
    private static HashMap<Class,Object> singletons = new HashMap<>();
    private static HashMap<Class,Object> overrides = new HashMap<>();

    private Container() {}

    public static <T> void bind(Class<T> clazz, Callable<T> factory) {
        bindings.put(clazz, factory);
    }

    public static <T> void bind(Class<T> clazz, Factory factory) {
        bindings.put(clazz, factory);
    }

    public static <T> void singleton(Class<T> clazz, Callable<T> factory) {
        singletons.put(clazz, null);
        bind(clazz, factory);
    }

    public static <T> void singleton(Class<T> clazz, Factory factory) {
        singletons.put(clazz, null);
        bind(clazz, factory);
    }

    public static <T> T resolve(Class<T> clazz) {
        if (overrides.get(clazz) != null) {
            return (T) overrides.get(clazz);
        }

        if (singletons.containsKey(clazz)) {
            if (singletons.get(clazz) == null) {
                singletons.put(clazz, createObject(clazz));
            }

            return (T) singletons.get(clazz);
        }

        return createObject(clazz);
    }

    public static <T> void instance(Class<T> clazz, Object instance) {
        overrides.put(clazz, instance);
    }

    public static void clear() {
        bindings = new HashMap<>();
        overrides = new HashMap<>();
        singletons = new HashMap<>();
    }

    private static <T> T createObject(Class<T> clazz) {
        if (bindings.get(clazz) instanceof Callable) {
            try {
                return (T) ((Callable) bindings.get(clazz)).call();
            } catch (Exception e) {
                throw new RuntimeException("Could not resolve " + clazz.getName() + " out of the container", e);
            }
        }

        if (bindings.get(clazz) instanceof Factory) {
            return (T) ((Factory) bindings.get(clazz)).create();
        }

        throw new RuntimeException("Could not resolve " + clazz.getName() + " out of the container");
    }
}
