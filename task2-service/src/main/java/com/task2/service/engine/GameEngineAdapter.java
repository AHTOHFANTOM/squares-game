package com.task2.service.engine;

import com.task1.console.Board;
import com.task1.console.GameEngine;
import com.task2.service.dto.BoardDto;
import com.task2.service.dto.GameStatusDto;
import com.task2.service.dto.SimpleMoveDto;

import java.util.List;
import java.util.Random;

/**
 * Адаптер для использования консольного движка игры (GameEngine)
 * в контексте REST API. Позволяет вычислять следующий ход и
 * получать статус игры на основе данных, полученных через DTO.
 */
public class GameEngineAdapter {

    /**
     * Вычисляет следующий ход для текущего игрока на основе состояния доски.
     *
     * @param boardDto DTO с информацией о текущем состоянии доски
     * @return SimpleMoveDto с координатами хода и цветом игрока,
     *         либо null, если ходов нет или игра завершена
     */
    public SimpleMoveDto calculateNextMove(BoardDto boardDto) {
        // Создаем доску из размера DTO
        Board board = new Board(boardDto.getSize());
        String data = boardDto.getData();
        int index = 0;

        // Заполняем доску текущим состоянием
        for (int row = 0; row < boardDto.getSize(); row++) {
            for (int col = 0; col < boardDto.getSize(); col++) {
                if (index < data.length()) {
                    char cell = data.charAt(index);
                    if (cell == 'w' || cell == 'W') board.makeMove(col, row, 'W');
                    else if (cell == 'b' || cell == 'B') board.makeMove(col, row, 'B');
                }
                index++;
            }
        }

        // Инициализируем движок игры
        GameEngine engine = new GameEngine();
        char computerColor = boardDto.getNextPlayerColor().toUpperCase().charAt(0);

        // Вычисляем следующий ход
        int[] move = engine.computeNextComputerMove(board, computerColor);

        // Если ходов нет, возвращаем null
        if (move == null) return null;

        // Возвращаем DTO с информацией о ходе
        return new SimpleMoveDto(move[0], move[1], boardDto.getNextPlayerColor());
    }

    /**
     * Создает объект Board на основе DTO.
     *
     * @param dto DTO с состоянием доски
     * @return объект Board, полностью инициализированный текущими ходами
     */
    private Board createBoardFromDto(BoardDto dto) {
        Board board = new Board(dto.getSize());
        String data = dto.getData();
        int index = 0;

        for (int row = 0; row < dto.getSize(); row++) {
            for (int col = 0; col < dto.getSize(); col++) {
                if (index < data.length()) {
                    char cell = data.charAt(index);
                    if (cell == 'w' || cell == 'W') {
                        board.makeMove(col, row, 'W');
                    } else if (cell == 'b' || cell == 'B') {
                        board.makeMove(col, row, 'B');
                    }
                }
                index++;
            }
        }

        return board;
    }

    /**
     * Получает текущий статус игры на основе DTO.
     * Проверяет победу белого или черного игрока, ничью или продолжающуюся игру.
     *
     * @param dto DTO с текущим состоянием доски
     * @return GameStatusDto с информацией о статусе игры и результате
     */
    public GameStatusDto getGameStatus(BoardDto dto) {
        Board board = createBoardFromDto(dto);

        if (board.hasSquare('W')) {
            return new GameStatusDto("finished", "W wins");
        } else if (board.hasSquare('B')) {
            return new GameStatusDto("finished", "B wins");
        } else if (board.isFull()) {
            return new GameStatusDto("finished", "Draw");
        } else {
            return new GameStatusDto("ongoing", null);
        }
    }
}
