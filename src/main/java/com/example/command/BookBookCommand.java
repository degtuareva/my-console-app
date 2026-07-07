package com.example.command;

import com.example.model.Booking;
import com.example.service.LibraryService;

import java.util.Scanner;

/**
 * Класс BookBookCommand - команда для бронирования книги читателем.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: обработка команды бронирования книги
 * <p>
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить проверку лимитов бронирования)
 *
 * @author Your Name
 * @version 1.0.0
 */
public class BookBookCommand implements Command {

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
    public BookBookCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды бронирования книги.
     *
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return результат выполнения команды
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Бронирование книги ===");

        // Сбор данных о бронировании
        System.out.print("Введите ID книги для бронирования: ");
        String bookIdInput = scanner.nextLine().trim();

        System.out.print("Введите ID читателя: ");
        String readerIdInput = scanner.nextLine().trim();

        // Парсинг ID
        Long bookId = parseId(bookIdInput);
        Long readerId = parseId(readerIdInput);

        // Проверка ввода
        if (bookId == null || readerId == null) {
            return "Ошибка: ID должны быть числами!";
        }

        // Бронирование книги через сервис
        try {
            Booking booking = libraryService.bookBook(bookId, readerId);
            return String.format(
                    "✓ Книга успешно забронирована!\n%s",
                    booking.toString()
            );
        } catch (RuntimeException e) {
            return "Ошибка: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка при бронировании: " + e.getMessage();
        }
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Бронировать книгу";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Забронировать книгу для читателя";
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