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
        Object requestAdapter;
        try {
            requestAdapter = clazz.getDeclaredConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Request.class))
                .forEach(method -> {
                    Request r = method.getAnnotation(Request.class);
                    switch (r.method()) {
                        case GET:
                            get(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            break;
                        case POST:
                            post(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            break;
                        case PUT:
                            put(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            break;
                        case DELETE:
                            delete(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            break;
                        case OPTIONS:
                            options(r.path(), (request, response) -> method.invoke(requestAdapter, request, response));
                            break;
                    }
                });
    }
}
