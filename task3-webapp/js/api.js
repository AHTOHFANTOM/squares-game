/**
 * GameAPI - модуль для работы с REST API сервера Squares Game.
 * Предоставляет функции для получения следующего хода компьютера,
 * проверки статуса игры, проверки здоровья сервера и его доступности.
 */
var GameAPI = (function() {
    'use strict';

    /** Базовый URL API */
    var BASE_URL = 'http://localhost:8080/api';
    /** Правила игры по умолчанию */
    var DEFAULT_RULES = 'standard';

    /**
     * Универсальная функция для отправки AJAX-запроса к серверу.
     *
     * @param {string} method - HTTP метод (GET, POST и т.д.)
     * @param {string} url - URL запроса
     * @param {object|null} data - Данные для отправки (JSON), если есть
     * @param {function(string|null, object|null)} callback - Колбэк с ошибкой и результатом
     */
    function makeRequest(method, url, data, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open(method, url, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                var response = null;
                var error = null;

                // Попытка распарсить JSON
                try {
                    if (xhr.responseText) {
                        response = JSON.parse(xhr.responseText);
                    }
                } catch (e) {
                    error = 'Failed to parse response: ' + e.message;
                }

                if (xhr.status >= 200 && xhr.status < 300) {
                    callback(error, response);
                } else {
                    error = error || 'Request failed with status: ' + xhr.status;
                    if (response && response.error) {
                        error = response.error;
                    }
                    callback(error, null);
                }
            }
        };

        xhr.onerror = function() {
            callback('Network error occurred', null);
        };

        var payload = data ? JSON.stringify(data) : null;
        xhr.send(payload);
    }

    /**
     * Преобразует игровую доску в строку для отправки на сервер.
     * Используется формат: 'w' = белая клетка, 'b' = черная, ' ' = пустая.
     *
     * @param {Array<Array<string>>} board - двумерный массив доски
     * @param {number} size - размер доски
     * @returns {string} строка, представляющая доску
     */
    function boardToString(board, size) {
        var result = '';
        for (var row = 0; row < size; row++) {
            for (var col = 0; col < size; col++) {
                var cell = board[row][col];
                if (cell === 'W') {
                    result += 'w';
                } else if (cell === 'B') {
                    result += 'b';
                } else {
                    result += ' ';
                }
            }
        }
        return result;
    }

    /**
     * Проверяет здоровье API сервера.
     *
     * @param {function(string|null, boolean)} callback - Колбэк с ошибкой и статусом
     */
    function checkHealth(callback) {
        makeRequest('GET', BASE_URL + '/health', null, function(error, response) {
            if (error) {
                callback(error, false);
            } else {
                var isHealthy = response && (response.message === 'OK' || response.status === 'OK');
                callback(null, isHealthy);
            }
        });
    }

    /**
     * Получает следующий ход компьютера от сервера.
     *
     * @param {object} gameState - Текущее состояние игры {size, board, currentPlayer}
     * @param {string|function} rules - Правила игры или callback (если rules не указаны)
     * @param {function} callback - Колбэк с ошибкой и объектом хода {x, y, color}
     */
    function getNextMove(gameState, rules, callback) {
        if (typeof rules === 'function') {
            callback = rules;
            rules = DEFAULT_RULES;
        }

        var boardDto = {
            size: gameState.size,
            data: boardToString(gameState.board, gameState.size),
            nextPlayerColor: gameState.currentPlayer
        };

        var url = BASE_URL + '/' + rules + '/nextMove';
        makeRequest('POST', url, boardDto, function(error, response) {
            if (error) {
                callback(error, null);
                return;
            }

            if (response && typeof response.x === 'number' && typeof response.y === 'number') {
                callback(null, {
                    x: response.x,
                    y: response.y,
                    color: response.color
                });
            } else if (response && response.message) {
                // Игра закончена или ход невозможен
                callback(null, null);
            } else {
                callback('Invalid response format', null);
            }
        });
    }

    /**
     * Получает текущий статус игры с сервера.
     *
     * @param {object} gameState - Текущее состояние игры {size, board, currentPlayer}
     * @param {function} callback - Колбэк с ошибкой и объектом статуса {status, result}
     */
    function getGameStatus(gameState, callback) {
        var boardDto = {
            size: gameState.size,
            data: boardToString(gameState.board, gameState.size),
            nextPlayerColor: gameState.currentPlayer
        };

        var url = BASE_URL + '/status';
        makeRequest('POST', url, boardDto, function(error, response) {
            if (error) {
                callback(error, null);
                return;
            }

            if (response && response.status) {
                callback(null, {
                    status: response.status,
                    result: response.result
                });
            } else {
                callback('Invalid response format', null);
            }
        });
    }

    /**
     * Проверяет доступность API с повторными попытками.
     *
     * @param {function} callback - Колбэк с ошибкой и статусом доступности
     * @param {number} [maxRetries=3] - Максимальное количество повторных попыток
     */
    function checkAvailability(callback, maxRetries) {
        maxRetries = maxRetries || 3;
        var retries = 0;

        function tryCheck() {
            checkHealth(function(error, isHealthy) {
                if (isHealthy) {
                    callback(null, true);
                } else if (retries < maxRetries) {
                    retries++;
                    setTimeout(tryCheck, 1000 * retries); // экспоненциальная задержка
                } else {
                    callback(error || 'API is not available after ' + maxRetries + ' attempts', false);
                }
            });
        }

        tryCheck();
    }

    // Публичный API модуля
    return {
        getNextMove: getNextMove,
        getGameStatus: getGameStatus,
        checkHealth: checkHealth,
        checkAvailability: checkAvailability,
        baseUrl: BASE_URL
    };
})();
