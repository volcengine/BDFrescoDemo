/**
 * Copyright 2022 Beijing Volcano Engine Technology Ltd.
 * SPDX-License-Identifier: MIT
 */

package com.bytedance.fresco.showsample.utils;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;

import java.lang.reflect.InvocationTargetException;

public class FragmentFactory {
    private static final SimpleArrayMap<String, Class<?>> sClassMap = new SimpleArrayMap<>();

    @NonNull
    private static Class<?> loadClass(@NonNull ClassLoader classLoader,
                                      @NonNull String className) throws ClassNotFoundException {
        Class<?> clazz = sClassMap.get(className);
        if (clazz == null) {
            // Class not found in the cache, see if it's real, and try to add it
            clazz = Class.forName(className, false, classLoader);
            sClassMap.put(className, clazz);
        }
        return clazz;
    }

    @NonNull
    public static Class<? extends Fragment> loadFragmentClass(@NonNull ClassLoader classLoader,
                                                              @NonNull String className) {
        try {
            Class<?> clazz = loadClass(classLoader, className);
            return (Class<? extends Fragment>) clazz;
        } catch (ClassNotFoundException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + className
                    + ": make sure class name exists", e);
        } catch (ClassCastException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + className
                    + ": make sure class is a valid subclass of Fragment", e);
        }
    }

    @NonNull
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        try {
            Class<? extends Fragment> cls = loadFragmentClass(classLoader, className);
            return cls.getConstructor().newInstance();
        } catch (java.lang.InstantiationException e) {
            throw new Fragment.InstantiationException("Unable instantiate fragment " + className
                    + " failed", e);
        } catch (IllegalAccessException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + className, e);
        } catch (NoSuchMethodException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + className
                    + " failed", e);
        } catch (InvocationTargetException e) {
            throw new Fragment.InstantiationException("Unable to instantiate fragment " + className
                    + " failed", e);
        }
    }
}