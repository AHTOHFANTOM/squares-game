package com.task2.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Глобальный обработчик исключений для REST API.
 * Перехватывает все необработанные исключения типа {@link Exception} и возвращает
 * клиенту ответ с кодом 500 (Internal Server Error) в формате JSON.
 * Использование:
 * - Позволяет централизованно обрабатывать ошибки.
 * - Не нужно ставить try/catch в каждом контроллере для необработанных исключений.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * Метод, который вызывается Jersey при возникновении необработанного исключения.
     *
     * @param e исключение, которое было выброшено
     * @return объект {@link Response} с HTTP статусом 500 и JSON с сообщением об ошибке
     */
    @Override
    public Response toResponse(Exception e) {
        // Формируем JSON-ответ с полем "error", содержащим сообщение исключения
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .build();
    }
}
