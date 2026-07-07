package com.example.command;

import com.example.service.LibraryService;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс CommandFactory - фабрика для создания и управления командами CLI.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: создание и управление командами
 *
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс зависит от абстракций (Command, LibraryService), а не от конкретных реализаций
 *
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить новые команды)
 *
 * @author Your Name
 * @version 1.0.0
 */
public class CommandFactory {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Сервис для работы с библиотекой */
    private final LibraryService libraryService;

    /** Список всех доступных команд */
    private final List<Command> commands;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостью (LibraryService).
     *
     * @param libraryService сервис для работы с библиотекой
     */
    public CommandFactory(LibraryService libraryService) {
        if (libraryService == null) {
            throw new IllegalArgumentException("LibraryService не может быть null");
        }

        this.libraryService = libraryService;
        this.commands = initializeCommands();
    }

    // ==================== МЕТОДЫ ====================

    /**
     * Метод для получения списка всех команд.
     *
     * @return список всех команд
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Метод для выполнения команды по номеру.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за выполнение команды по номеру
     *
     * @param commandNumber номер команды (1-based)
     * @param scanner объект Scanner для чтения ввода
     * @return результат выполнения команды
     */
    public String executeCommand(int commandNumber, java.util.Scanner scanner) {
        // Проверка на корректность номера
        if (commandNumber < 1 || commandNumber > commands.size()) {
            return "Ошибка: Неверный номер команды!";
        }

        // Получение команды по номеру
        Command command = commands.get(commandNumber - 1);

        // Выполнение команды
        return command.execute(scanner);
    }

    /**
     * Метод для отображения меню команд.
     *
     * @return строка с меню команд
     */
    public String showMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("\n========================================\n");
        menu.append("       Меню библиотеки\n");
        menu.append("========================================\n");

        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            menu.append(String.format(
                    "%d. %s - %s\n",
                    i + 1,
                    command.getName(),
                    command.getDescription()
            ));
        }

        menu.append("========================================\n");
        return menu.toString();
    }

    /**
     * Метод для инициализации списка команд.
     *
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за создание списка команд
     *
     * ПРИНЦИП SOLID: Open/Closed Principle
     * - Метод открыт для расширения (можно добавить новые команды)
     *
     * @return список всех команд
     */
    private List<Command> initializeCommands() {
        List<Command> commandList = new ArrayList<>();

        // Добавление команд в меню
        commandList.add(new AddBookCommand(libraryService));
        commandList.add(new ListBookingsCommand(libraryService));
        commandList.add(new SearchBooksCommand(libraryService));
        commandList.add(new AddReaderCommand(libraryService));
        commandList.add(new BookBookCommand(libraryService));
        commandList.add(new ReturnBookCommand(libraryService));
        commandList.add(new ListBookingsCommand(libraryService));
        commandList.add(new ShowStatsCommand(libraryService));
        commandList.add(new ExitCommand());

        return commandList;
    }
}