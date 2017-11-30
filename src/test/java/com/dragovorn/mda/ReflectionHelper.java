package com.dragovorn.mda;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

    public static <T>void injectStaticFinal(Class clazz, String fieldName, Class<T> type, T value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true); // Make our field accessible
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL); // Make our field not final
            field.set(null, value); // inject our new value
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}