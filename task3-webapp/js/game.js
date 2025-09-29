/**
 * SquaresGame
 *
 * Модуль для управления логикой игры "Squares" на фронтенде.
 * Обеспечивает взаимодействие с API, рендер доски, обработку ходов игрока и компьютера,
 * а также отображение состояния игры и сообщений пользователю.
 *
 * Состояние игры хранится в объекте `gameState`.
 */
var SquaresGame = (function() {
    'use strict';

    /**
     * @typedef {Object} GameState
     * @property {Array<Array<string|null>>} board - Текущее состояние доски
     * @property {number} size - Размер доски (NxN)
     * @property {string} playerColor - Цвет игрока ('w' или 'b')
     * @property {string} computerColor - Цвет компьютера ('w' или 'b')
     * @property {string} currentPlayer - Цвет текущего игрока ('w' или 'b')
     * @property {boolean} isGameActive - Флаг активности игры
     * @property {boolean} isPlayerTurn - Флаг текущего хода игрока
     * @property {number} moveCount - Счетчик сделанных ходов
     */

    /**
     * Текущее состояние игры
     * @type {GameState}
     */

    var gameState = {
        board: [],
        size: 5,
        playerColor: 'w',
        computerColor: 'b',
        currentPlayer: 'w',
        isGameActive: false,
        isPlayerTurn: true,
        moveCount: 0
    };

    /** DOM элементы игры */
    var elements = {};

    /**
     * Инициализация игры
     * Вызывается при загрузке страницы.
     */

    function init() {
        initElements();
        bindEvents();
        checkApiAvailability();
        createBoard(gameState.size);
        setButtonStates(false, false);
    }

    /**
     * Инициализация DOM элементов
     */
    function initElements() {
        elements.boardSizeSelect = document.getElementById('board-size');
        elements.playerColorSelect = document.getElementById('player-color');
        elements.startGameBtn = document.getElementById('start-game');
        elements.resetGameBtn = document.getElementById('reset-game');
        elements.gameBoard = document.getElementById('game-board');
        elements.turnIndicator = document.getElementById('turn-indicator');
        elements.gameMessage = document.getElementById('game-message');
    }

    /**
     * Привязка обработчиков событий к кнопкам и клеткам
     */
    function bindEvents() {
        elements.startGameBtn.addEventListener('click', startGame);
        elements.resetGameBtn.addEventListener('click', resetGame);
    }

    /**
     * Проверка доступности API
     */
    function checkApiAvailability() {
        showMessage('Проверка соединения с API...', 'info');
        GameAPI.checkAvailability(function(error, isAvailable) {
            if (isAvailable) {
                showMessage('API подключено. Готово к игре!', 'success');
            } else {
                showMessage('Предупреждение: Не удается подключиться к API. Компьютер не сможет ходить.', 'error');
            }
        });
    }

    /**
     * Запуск новой игры
     */
    function startGame() {
        var size = parseInt(elements.boardSizeSelect.value, 10);
        var playerColor = elements.playerColorSelect.value;

        gameState.size = size;
        gameState.playerColor = playerColor;
        gameState.computerColor = playerColor === 'w' ? 'b' : 'w';
        gameState.currentPlayer = 'w';
        gameState.isGameActive = true;
        gameState.moveCount = 0;
        gameState.isPlayerTurn = (gameState.currentPlayer === gameState.playerColor);
        initBoard(size);
        createBoard(size);
        clearBoard();
        updateTurnIndicator();
        setButtonStates(true, gameState.isPlayerTurn);
        showMessage('Игра началась!');
        if (!gameState.isPlayerTurn) {
            makeComputerMove();
        }
    }

    /**
     * Сброс текущей игры
     */
    function resetGame() {
        gameState.isGameActive = false;
        clearBoard();
        showMessage('Нажмите «Начать игру», чтобы начать');
        setButtonStates(false, false);
    }

    /**
     * Инициализация внутреннего состояния доски
     * @param {number} size - Размер доски
     */
    function initBoard(size) {
        gameState.board = [];
        for (var i = 0; i < size; i++) {
            var row = [];
            for (var j = 0; j < size; j++) {
                row.push(null);
            }
            gameState.board.push(row);
        }
    }

    /**
     * Обработка клика по клетке игроком
     * @param {number} row - Индекс строки
     * @param {number} col - Индекс колонки
     */
    function onCellClick(row, col) {
        if (!gameState.isGameActive || !gameState.isPlayerTurn || gameState.board[row][col] !== null) {
            return;
        }

        gameState.board[row][col] = gameState.playerColor.toUpperCase();
        updateCell(row, col, gameState.playerColor.toUpperCase(), true);
        gameState.moveCount++;

        checkGameStatus(function(error, status) {
            if (error) {
                showMessage('Ошибка проверки статуса: ' + error, 'error');
                return;
            }
            if (status.status === 'finished') {
                handleGameEnd(status.result);
                return;
            }

            switchTurn();
            makeComputerMove();
        });
    }

    /**
     * Выполнение хода компьютера через API
     */
    function makeComputerMove() {
        showMessage('Компьютер думает...', 'info');
        setButtonStates(true, false);

        setTimeout(function() {
            GameAPI.getNextMove(gameState, function(error, move) {
                if (error) {
                    showMessage('Ошибка хода компьютера: ' + error + '.Проверьте подключение к API.', 'error');
                    return;
                }
                if (!move) {
                    checkGameStatus(function(err, status) {
                        if (status.status === 'finished') {
                            handleGameEnd(status.result);
                        }
                    });
                    return;
                }

                var x = move.x;
                var y = move.y;
                gameState.board[y][x] = gameState.computerColor.toUpperCase();
                updateCell(y, x, gameState.computerColor.toUpperCase(), true);
                gameState.moveCount++;

                checkGameStatus(function(error, status) {
                    if (error) {
                        showMessage('Ошибка проверки статуса: ' + error, 'error');
                        return;
                    }
                    if (status.status === 'finished') {
                        handleGameEnd(status.result);
                        return;
                    }

                    switchTurn();
                });
            });
        }, 500);
    }

    /**
     * Смена текущего игрока
     */
    function switchTurn() {
        gameState.currentPlayer = gameState.currentPlayer === 'w' ? 'b' : 'w';
        gameState.isPlayerTurn = (gameState.currentPlayer === gameState.playerColor);
        updateTurnIndicator();
        setButtonStates(true, gameState.isPlayerTurn);
    }

    /**
     * Проверка статуса игры через API
     * @param {function(Error, Object)} callback - Колбэк с результатом
     */
    function checkGameStatus(callback) {
        GameAPI.getGameStatus(gameState, function(error, response) {
            if (error) {
                callback(error, null);
                return;
            }
            callback(null, response);
        });
    }

    /**
     * Обработка окончания игры
     * @param {string} result - Результат игры ('Draw', 'W wins', 'B wins')
     */
    function handleGameEnd(result) {
        gameState.isGameActive = false;
        setButtonStates(false, false);
        var message;
        if (result === 'Draw') {
            message = 'Игра закончилась вничью!';
        } else if (result === gameState.playerColor.toUpperCase() + ' wins') {
            message = 'Вы выиграли!';
        } else {
            message = 'Компьютер выиграл!';
        }
        showMessage(message, 'success');
    }

    /**
     * Создание визуальной доски
     * @param {number} size - Размер доски
     */
    function createBoard(size) {
        elements.gameBoard.innerHTML = '';
        elements.gameBoard.style.gridTemplateColumns = 'repeat(' + size + ', 1fr)';
        for (var row = 0; row < size; row++) {
            for (var col = 0; col < size; col++) {
                var cell = document.createElement('button');
                cell.className = 'cell';
                cell.dataset.row = row.toString();
                cell.dataset.col = col.toString();
                (function(r, c) {
                    cell.addEventListener('click', function() {
                        onCellClick(r, c);
                    });
                })(row, col);
                elements.gameBoard.appendChild(cell);
            }
        }
    }

    /**
     * Обновление конкретной клетки
     * @param {number} row - Индекс строки
     * @param {number} col - Индекс колонки
     * @param {string} value - Цвет ('W', 'B')
     * @param {boolean} isLastMove - Флаг последнего хода
     */
    function updateCell(row, col, value, isLastMove) {
        var cell = elements.gameBoard.querySelector('[data-row="' + row + '"][data-col="' + col + '"]');
        if (!cell) return;
        cell.classList.remove('white', 'black', 'occupied', 'last-move');
        cell.textContent = '';
        if (value === 'W') {
            cell.classList.add('white', 'occupied');
            cell.textContent = '●';
        } else if (value === 'B') {
            cell.classList.add('black', 'occupied');
            cell.textContent = '●';
        }
        if (isLastMove) {
            cell.classList.add('last-move');
        }
    }

    /**
     * Очистка визуальной доски
     */
    function clearBoard() {
        var cells = elements.gameBoard.querySelectorAll('.cell');
        for (var i = 0; i < cells.length; i++) {
            var cell = cells[i];
            cell.classList.remove('white', 'black', 'occupied', 'last-move');
            cell.textContent = '';
        }
    }

    /**
     * Обновление индикатора текущего хода
     */
    function updateTurnIndicator() {
        var displayColor = gameState.currentPlayer === 'w' ? 'Белые' : 'Черные';
        var turnType = gameState.isPlayerTurn ? 'Ваш ход' : 'Ход компьютера';
        elements.turnIndicator.textContent = displayColor + ' (' + turnType + ')';
        elements.turnIndicator.className = gameState.currentPlayer === 'w' ? 'white' : 'black';
    }

    /**
     * Показ сообщения пользователю
     * @param {string} message - Сообщение
     * @param {string} type - Тип ('info', 'success', 'error')
     */
    function showMessage(message, type) {
        elements.gameMessage.textContent = message;
        elements.gameMessage.className = elements.gameMessage.className.replace(/\b(info|success|error)\b/g, '');
        if (type) {
            elements.gameMessage.className += ' ' + type;
        }
    }

    /**
     * Установка доступности кнопок и клеток
     * @param {boolean} gameActive - Игра активна
     * @param {boolean} playerTurn - Ход игрока
     */
    function setButtonStates(gameActive, playerTurn) {
        elements.startGameBtn.disabled = gameActive;
        elements.resetGameBtn.disabled = !gameActive;
        var cells = elements.gameBoard.querySelectorAll('.cell:not(.occupied)');
        for (var i = 0; i < cells.length; i++) {
            cells[i].disabled = !gameActive || !playerTurn;
        }
    }

    document.addEventListener('DOMContentLoaded', init);

    // Публичные методы
    return {
        getGameState: function() { return gameState; },
        startGame: startGame,
        resetGame: resetGame
    };
})();