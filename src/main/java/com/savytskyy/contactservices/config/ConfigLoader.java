package com.savytskyy.contactservices.config;

import com.savytskyy.contactservices.annotations.SystemProp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ConfigLoader {


    public <T> T getSystemProps(Class<T> c) {
        Object object = createObject(c);
        extractProperties(object,System.getProperties());
        return (T)object;
    }

    public <T> T getFileProps(Class<T> c, String file) {
        Object object = createObject(c);
        try (InputStream is = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(is);
            extractProperties(object, props);
        } catch (IOException e) {
            throw new RuntimeException("Fail load properties form "+file,e);
        }
        return (T)object;
    }

    private Object createObject (Class c) {
        try {
            Constructor constructor=c.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException|IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("NEED DEFAULT CONSTRUCTOR",e);
        }
    }

    public void extractProperties(Object object, Properties props) {
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(SystemProp.class)) {
                SystemProp ann = field.getAnnotation(SystemProp.class);
                String propName = ann.value();
                String value = props.getProperty(propName);
                field.setAccessible(true);
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
