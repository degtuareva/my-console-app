package com.example.command;

import com.example.service.LibraryService;

import java.util.Scanner;

/**
 * Класс ReturnBookCommand - команда для возврата книги читателем.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: обработка команды возврата книги
 *
 * @author Your Name
 * @version 1.0.0
 */
public class ReturnBookCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * Сервис для работы с библиотекой
     */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью.
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public ReturnBookCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды возврата книги.
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Возврат книги ===");

        // Сбор данных о бронировании
        System.out.print("Введите ID бронирования для возврата: ");
        String bookingIdInput = scanner.nextLine().trim();

        // Парсинг ID
        Long bookingId = parseId(bookingIdInput);

        // Проверка ввода
        if (bookingId == null) {
            return "Ошибка: ID должен быть числом!";
        }

        // Возврат книги через сервис
        boolean success = libraryService.returnBook(bookingId);

        if (success) {
            return "✓ Книга успешно возвращена!";
        } else {
            return "Ошибка: Бронирование не найдено или книга уже возвращена!";
        }
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Возврат книги";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Возвратить забронированную книгу";
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Метод для парсинга ID из строки.
     *
     * @param idInput строка с ID
     * @return Long с ID или null если ошибка
     */
    private Long parseId(String idInput) {
        try {
            return Long.parseLong(idInput);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}