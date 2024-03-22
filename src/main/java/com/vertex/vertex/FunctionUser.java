package com.vertex.vertex;

import com.vertex.vertex.user.model.entity.User;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

@AllArgsConstructor
public class FunctionUser implements Function<Method, Void> {

    private final User user;

    @Override
    public Void apply(Method method) {
        try {
            method.invoke(user);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
