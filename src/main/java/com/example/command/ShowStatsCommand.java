package com.example.command;

import com.example.service.LibraryService;
import java.util.Scanner;

/**
 * Класс ShowStatsCommand - команда для отображения статистики библиотеки.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: отображение статистики
 *
 * @author Your Name
 * @version 1.0.0
 */
public class ShowStatsCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Сервис для работы с библиотекой */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью.
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public ShowStatsCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды отображения статистики.
     *
     * @param scanner объект Scanner (не используется)
     * @return результат выполнения команды (статистика)
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Статистика библиотеки ===");

        // Получение статистики через сервис
        int bookCount = libraryService.getBookCount();
        int readerCount = libraryService.getReaderCount();
        int activeBookingCount = libraryService.getActiveBookingCount();

        // Отображение статистики
        return String.format(
                "Всего книг: %d\n" +
                        "Всего читателей: %d\n" +
                        "Активных бронирований: %d",
                bookCount,
                readerCount,
                activeBookingCount
        );
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Статистика";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Показать статистику библиотеки";
    }
}