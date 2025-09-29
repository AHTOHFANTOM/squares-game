# Squares Game

Игра "Квадраты".  
В проекте реализованы три части: консольная игра на Java, веб-сервис для логики игры и фронтенд на HTML/JS.

---

## Структура проекта

- **`task1-console/`** - консольное приложение (Java 8, Maven)
- **`task2-service/`** - web-сервис (Java 8, Maven, JAX-RS, Jersey)
- **`task3-webapp/`** - веб-приложение (HTML + CSS + JS ES5)

---

## Запуск проекта

### 1. Консольная игра

```bash
cd task1-console
mvn clean install
java -jar target/squares-console.jar
```

- Собираем проект через install для следующего задания

### 2. Web-сервис

```bash
cd task2-service
mvn clean package
java -jar target/squares-service.jar
```

Сервис будет доступен по адресу http://localhost:8080/api

### 3. Веб-игра (frontend)

```bash
cd task3-webapp
npm install
npm start
```

Приложение поднимется по адресу http://localhost:3000/