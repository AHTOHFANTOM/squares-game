package com.task2.service.controller;

import com.task2.service.dto.BoardDto;
import com.task2.service.dto.GameStatusDto;
import com.task2.service.dto.SimpleMoveDto;
import com.task2.service.engine.GameEngineAdapter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST-контроллер для работы с игрой.
 * Предоставляет эндпоинты для получения следующего хода, проверки статуса игры и проверки состояния сервиса.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameController {

    /**
     * Адаптер игрового движка для расчёта ходов и получения статуса игры.
     */
    private final GameEngineAdapter gameEngine = new GameEngineAdapter();

    /**
     * Эндпоинт для получения следующего хода компьютера.
     *
     * @param rules Строка правил игры (не используется в текущей версии, зарезервировано для расширения)
     * @param boardDto DTO текущего состояния доски
     * @return Response с ходом компьютера или сообщением о завершении игры
     */
    @POST
    @Path("/{rules}/nextMove")
    public Response getNextMove(@PathParam("rules") String rules, BoardDto boardDto) {
        try {
            // Проверка на null
            if (boardDto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"BoardDto cannot be null\"}")
                        .build();
            }

            // Проверка минимального размера доски
            if (boardDto.getSize() <= 2) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Board size must be greater than 2\"}")
                        .build();
            }

            // Проверка наличия данных доски
            if (boardDto.getData() == null || boardDto.getData().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Board data cannot be null or empty\"}")
                        .build();
            }

            // Проверка корректного цвета следующего игрока
            if (boardDto.getNextPlayerColor() == null ||
                    (!boardDto.getNextPlayerColor().equalsIgnoreCase("w") &&
                            !boardDto.getNextPlayerColor().equalsIgnoreCase("b"))) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"nextPlayerColor must be 'w' or 'b'\"}")
                        .build();
            }

            // Расчёт следующего хода через адаптер
            SimpleMoveDto nextMove = gameEngine.calculateNextMove(boardDto);

            // Если ход невозможен (игра окончена или нет свободных клеток)
            if (nextMove == null) {
                return Response.status(Response.Status.OK)
                        .entity("{\"message\":\"Game finished or no moves available\"}")
                        .build();
            }

            // Возврат корректного хода
            return Response.ok(nextMove).build();

        } catch (Exception e) {
            // Логирование ошибки и возврат 500
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal server error\"}")
                    .build();
        }
    }

    /**
     * Эндпоинт для получения текущего статуса игры.
     *
     * @param boardDto DTO текущего состояния доски
     * @return Response с текущим статусом игры
     */
    @POST
    @Path("/status")
    public Response getGameStatus(BoardDto boardDto) {
        try {
            // Проверки аналогичные getNextMove
            if (boardDto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"BoardDto cannot be null\"}")
                        .build();
            }

            if (boardDto.getSize() <= 2) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Board size must be greater than 2\"}")
                        .build();
            }
            if (boardDto.getData() == null || boardDto.getData().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Board data cannot be null or empty\"}")
                        .build();
            }
            if (boardDto.getNextPlayerColor() == null ||
                    (!boardDto.getNextPlayerColor().equalsIgnoreCase("w") &&
                            !boardDto.getNextPlayerColor().equalsIgnoreCase("b"))) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"nextPlayerColor must be 'w' or 'b'\"}")
                        .build();
            }

            // Получение статуса игры через адаптер
            GameStatusDto status = gameEngine.getGameStatus(boardDto);
            return Response.ok(status).build();

        } catch (Exception e) {
            System.err.println("Error processing status request: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal server error\"}")
                    .build();
        }
    }

    /**
     * Эндпоинт проверки здоровья сервиса.
     *
     * @return Response со статусом "OK"
     */
    @GET
    @Path("/health")
    public Response health() {
        return Response.ok("{\"status\":\"OK\"}").build();
    }
}
