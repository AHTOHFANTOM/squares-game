package com.task2.service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Фильтр для обработки CORS (Cross-Origin Resource Sharing) запросов.
 * Позволяет клиентам с других доменов делать запросы к нашему API.
 * Реализует два интерфейса:
 * - ContainerRequestFilter: для перехвата входящих запросов, включая OPTIONS preflight.
 * - ContainerResponseFilter: для добавления заголовков CORS к ответам.
 */
@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    /**
     * Обработка входящего запроса.
     * Если метод OPTIONS (preflight), немедленно возвращаем ответ с заголовками CORS
     * и статусом 200 OK, без дальнейшей обработки запроса.
     *
     * @param requestContext контекст запроса
     * @throws IOException исключение ввода/вывода
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("OPTIONS".equals(requestContext.getMethod())) {
            // Preflight запрос: отвечаем сразу с CORS заголовками
            requestContext.abortWith(Response.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Allow-Headers",
                            "Origin, Content-Type, Accept, Authorization, X-Requested-With")
                    .header("Access-Control-Max-Age", "86400") // Кэшировать preflight на 1 день
                    .build());
        }
    }

    /**
     * Добавляет заголовки CORS к каждому исходящему ответу.
     * OPTIONS запросы обрабатываются в методе выше, поэтому здесь исключаем их.
     *
     * @param requestContext  контекст запроса
     * @param responseContext контекст ответа
     * @throws IOException исключение ввода/вывода
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        if (!"OPTIONS".equals(requestContext.getMethod())) {
            // Добавляем стандартные CORS заголовки к ответу
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers",
                    "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        }
    }
}
