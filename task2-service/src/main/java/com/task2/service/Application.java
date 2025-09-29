package com.task2.service;

import com.task2.service.controller.GameController;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

/**
 * Основной класс приложения Squares Game Web Service.
 * Отвечает за конфигурацию и запуск HTTP-сервера Grizzly с Jersey.
 * Настройки сервера загружаются из файла config.properties.
 */
public class Application {

    /** Базовый URI сервиса, загружается из config.properties */
    private static String BASE_URI;

    // Статический блок инициализации: загружает базовый URI из конфигурации
    static {
        try (InputStream input = Application.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                BASE_URI = prop.getProperty("base.uri", "http://localhost:8080/");
            } else {
                BASE_URI = "http://localhost:8080/";
            }
        } catch (IOException e) {
            // При ошибке загрузки используем URI по умолчанию
            BASE_URI = "http://localhost:8080/";
        }
    }

    /**
     * Настраивает и запускает Grizzly HTTP сервер с ресурсами Jersey.
     *
     * @return объект HttpServer, который можно остановить через shutdownNow()
     */
    public static HttpServer startServer() {
        // Конфигурация ресурсов Jersey
        final ResourceConfig config = new ResourceConfig()
                .register(GameController.class)   // Регистрируем контроллер игры
                .register(JacksonFeature.class)   // Поддержка JSON через Jackson
                .register(CorsFilter.class)       // Поддержка CORS
                .register(GlobalExceptionMapper.class); // Глобальный обработчик исключений

        // Создаем HTTP сервер по BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    /**
     * Главный метод запуска приложения.
     * Настраивает сервер, выводит информацию в консоль и ожидает завершения.
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        // Информационные сообщения
        System.out.println("Squares Game Web Service started.");
        System.out.println("Jersey app started with endpoints available at " + BASE_URI);
        System.out.println("API endpoint: " + BASE_URI + "api/{rules}/nextMove");
        System.out.println("Health check: " + BASE_URI + "api/health");
        System.out.println("Hit Ctrl+C to stop it...");

        // Добавляем обработчик корректного завершения сервера при остановке JVM
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        try {
            // Основной поток ждет завершения сервера
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Если поток прерван, корректно останавливаем сервер
            server.shutdownNow();
        }
    }
}
