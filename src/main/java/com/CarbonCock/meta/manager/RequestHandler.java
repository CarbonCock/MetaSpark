package com.CarbonCock.meta.manager;

import com.CarbonCock.meta.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;
import static spark.Spark.options;

public class RequestHandler {

    private RequestHandler(){ }

    public static void registerHandler(RequestAdapter adapter){
        Class<? extends RequestAdapter> clazz = adapter.getClass();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Request.class))
                .forEach(method -> {
                    Request r = method.getAnnotation(Request.class);
                    switch(r.method()){
                        case GET -> {
                            try {
                                Object requestAdapter = clazz.getDeclaredConstructor().newInstance();
                                get(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case POST -> {
                            try {
                                Object requestAdapter = clazz.getDeclaredConstructor().newInstance();
                                post(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case PUT -> {
                            try {
                                Object requestAdapter = clazz.getDeclaredConstructor().newInstance();
                                put(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case DELETE -> {
                            try {
                                Object requestAdapter = clazz.getDeclaredConstructor().newInstance();
                                delete(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case OPTIONS -> {
                            try {
                                Object requestAdapter = clazz.getDeclaredConstructor().newInstance();
                                options(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }
}
