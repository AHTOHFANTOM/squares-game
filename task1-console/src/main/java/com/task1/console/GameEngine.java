package com.task1.console;

import java.util.List;
import java.util.Random;

/**
 * Класс GameEngine реализует основную логику игры "Квадраты".
 * Он управляет игровым полем (Board), игроками и очередностью ходов.
 * Также отвечает за ходы компьютера и проверку окончания игры.
 */
public class GameEngine {

    /** Игровое поле */
    private Board board;

    /** Первый игрок */
    private Player player1;

    /** Второй игрок */
    private Player player2;

    /** Игрок, который ходит в текущий момент */
    private Player currentPlayer;

    /** Флаг активности игры */
    private boolean gameActive = false;

    /**
     * Начинает новую игру.
     * Проверяет корректность команд, создаёт игроков и доску.
     * Если первый игрок - компьютер, делает его первый ход.
     *
     * @param args массив аргументов команды GAME: [размер доски, игрок1, игрок2]
     */
    public void startNewGame(String[] args) {
        try {
            validateGameCommand(args);

            int size = Integer.parseInt(args[0].trim());
            if (size <= 2) {
                isIncorrectCommand();
                return;
            }

            player1 = parsePlayer(args[1].trim());
            player2 = parsePlayer(args[2].trim());

            if (player1.getColor() == player2.getColor()) {
                isIncorrectCommand();
                return;
            }

            board = new Board(size);
            gameActive = true;
            currentPlayer = player1;
            System.out.println("New game started");

            // Если первый игрок компьютер - делаем его ход
            if (currentPlayer.isComputer()) {
                makeComputerMove();
            }
        } catch (IllegalArgumentException e) {
            isIncorrectCommand();
        }
    }

    /**
     * Проверяет корректность команды GAME по количеству аргументов.
     *
     * @param args массив аргументов команды
     */
    private void validateGameCommand(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments for GAME");
        }
    }

    /**
     * Разбирает строку с информацией об игроке.
     * Ожидаемый формат: "TYPE COLOR", где TYPE = "user" или "comp", COLOR = "W" или "B"
     *
     * @param str строка с описанием игрока
     * @return объект Player
     */
    private Player parsePlayer(String str) {
        String[] parts = str.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid player format: " + str);
        }

        String type = parts[0].toLowerCase();
        if (!type.equals("user") && !type.equals("comp")) {
            throw new IllegalArgumentException("Invalid player type: " + type);
        }

        if (parts[1].length() != 1) {
            throw new IllegalArgumentException("Invalid color format: " + parts[1]);
        }

        char color = Character.toUpperCase(parts[1].charAt(0));
        if (color != 'W' && color != 'B') {
            throw new IllegalArgumentException("Invalid color: " + color);
        }

        return new Player(type, color);
    }

    /**
     * Выполняет ход пользователя.
     * Проверяет корректность ввода, делает ход на доске,
     * выводит информацию и проверяет окончание игры.
     *
     * @param args массив аргументов команды MOVE: [X, Y]
     */
    public void makeUserMove(String[] args) {
        if (!gameActive) {
            isIncorrectCommand();
            return;
        }

        if (currentPlayer.isComputer()) {
            isIncorrectCommand();
            return;
        }

        try {
            validateMoveCommand(args);

            int x = Integer.parseInt(args[0].trim());
            int y = Integer.parseInt(args[1].trim());

            if (!board.makeMove(x, y, currentPlayer.getColor())) {
                isIncorrectCommand();
                return;
            }

            System.out.printf("%c (%d, %d)%n", currentPlayer.getColor(), x, y);

            if (checkGameEnd()) {
                return;
            }

            switchTurn();
        } catch (IllegalArgumentException e) {
            isIncorrectCommand();
        }
    }

    /**
     * Проверяет корректность команды MOVE по количеству аргументов.
     *
     * @param args массив аргументов команды MOVE
     */
    private void validateMoveCommand(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments for MOVE");
        }
    }

    /**
     * Делает ход компьютера.
     * Вычисляет следующий ход, выполняет его и проверяет окончание игры.
     */
    private void makeComputerMove() {
        if (!gameActive) return;

        int[] move = computeNextComputerMove(this.board, currentPlayer.getColor());
        if (move == null) {
            finishDraw();
            return;
        }

        executeMove(move);
    }

    /**
     * Вычисляет следующий ход компьютера.
     * Логика:
     * 1. Если есть выигрышный ход для компьютера - делаем его.
     * 2. Если есть ход, блокирующий соперника, - делаем его с вероятностью 30%.
     * 3. Иначе выбираем случайную свободную клетку.
     *
     * @param board текущее состояние доски
     * @param computerColor цвет компьютера ('W' или 'B')
     * @return массив [X, Y] следующего хода или null, если ход невозможен
     */
    public int[] computeNextComputerMove(Board board, char computerColor) {
        List<int[]> freeCells = board.getFreeCells();
        if (freeCells.isEmpty()) return null;

        // Проверяем выигрышный ход для компьютера
        for (int[] cell : freeCells) {
            Board simulatedBoard = new Board(board);
            simulatedBoard.makeMove(cell[0], cell[1], computerColor);
            if (simulatedBoard.hasSquare(computerColor)) {
                return cell;
            }
        }

        // Проверяем возможность блокировки соперника
        char opponentColor = (computerColor == 'W') ? 'B' : 'W';
        Random random = new Random();
        for (int[] cell : freeCells) {
            Board simulatedBoard = new Board(board);
            simulatedBoard.makeMove(cell[0], cell[1], opponentColor);
            if (simulatedBoard.hasSquare(opponentColor)) {
                if (random.nextInt(100) < 30) { // вероятность блокировки 30%
                    return cell;
                }
            }
        }

        // Случайный ход
        return freeCells.get(new Random().nextInt(freeCells.size()));
    }

    /**
     * Выполняет ход (компьютера или пользователя) на доске,
     * выводит информацию и проверяет окончание игры.
     *
     * @param move массив [X, Y] хода
     */
    private void executeMove(int[] move) {
        int x = move[0];
        int y = move[1];
        board.makeMove(x, y, currentPlayer.getColor());
        System.out.printf("%c (%d, %d)%n", currentPlayer.getColor(), x, y);

        if (checkGameEnd()) {
            return;
        }

        switchTurn();
    }

    /**
     * Смена текущего игрока.
     * Если новый игрок - компьютер, делает его ход автоматически.
     */
    private void switchTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        if (currentPlayer.isComputer()) {
            makeComputerMove();
        }
    }

    /**
     * Проверяет, закончилась ли игра.
     * Условия окончания:
     * 1. Компьютер/пользователь собрал квадрат - победа.
     * 2. Доска заполнена - ничья.
     *
     * @return true, если игра закончена
     */
    private boolean checkGameEnd() {
        if (board.hasSquare(currentPlayer.getColor())) {
            System.out.printf("Game finished. %c wins!%n", currentPlayer.getColor());
            gameActive = false;
            return true;
        }

        if (board.isFull()) {
            finishDraw();
            return true;
        }

        return false;
    }

    /**
     * Завершает игру с объявлением ничьи.
     */
    private void finishDraw() {
        System.out.println("Game finished. Draw");
        gameActive = false;
    }

    /**
     * Выводит сообщение о некорректной команде.
     */
    protected void isIncorrectCommand() {
        System.out.println("Incorrect command");
    }
}
