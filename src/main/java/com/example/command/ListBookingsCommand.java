package com.example.command;

import com.example.service.LibraryService;
import com.example.model.Booking;
import java.util.List;
import java.util.Scanner;

/**
 * Класс ListBookingsCommand - команда для отображения списка всех бронирований.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: отображение списка бронирований
 *
 * @author Your Name
 * @version 1.0.0
 */
public class ListBookingsCommand implements Command {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Сервис для работы с библиотекой */
    private final LibraryService libraryService;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью.
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public ListBookingsCommand(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ==================== МЕТОДЫ ИНТЕРФЕЙСА Command ====================

    /**
     * Метод для выполнения команды отображения бронирований.
     *
     * @param scanner объект Scanner (не используется)
     * @return результат выполнения команды
     */
    @Override
    public String execute(Scanner scanner) {
        System.out.println("\n=== Список всех бронирований ===");
        System.out.println("1. Все бронирования");
        System.out.println("2. Только активные бронирования");
        System.out.print("Выберите вариант (1-2): ");

        String choice = scanner.nextLine().trim();
        List<Booking> bookings;

        if ("1".equals(choice)) {
            bookings = libraryService.getAllBookings();
        } else if ("2".equals(choice)) {
            bookings = libraryService.getActiveBookings();
        } else {
            return "Ошибка: Неверный выбор!";
        }

        // Проверка на пустоту
        if (bookings.isEmpty()) {
            return "Бронирований нет.";
        }

        // Отображение бронирований
        StringBuilder result = new StringBuilder();
        result.append(String.format("Всего бронирований: %d\n\n", bookings.size()));

        for (Booking booking : bookings) {
            result.append(String.format(
                    "[%d] %s\n",
                    booking.getId(),
                    booking.toString()
            ));
        }

        return result.toString();
    }

    /**
     * Метод для получения названия команды.
     *
     * @return название команды
     */
    @Override
    public String getName() {
        return "Список бронирований";
    }

    /**
     * Метод для получения описания команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "Показать все бронирования книг";
    }
}