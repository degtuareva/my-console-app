package com.example.dao;

import com.example.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Класс BookDao (Data Access Object для книги) - предоставляет доступ к данным книг.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: управление данными книг
 * - Не отвечает за бизнес-логику, отображение или валидацию
 * <p>
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс работает с абстракциями (Book), а не с конкретными реализациями БД
 * - В реальном проекте можно заменить на работу с PostgreSQL/MySQL
 * <p>
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить методы поиска, фильтрации)
 * - Класс закрыт для изменения (не нужно менять код при добавлении новых методов)
 *
 * @author Your Name
 * @version 1.0.0
 */
public class BookDao {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * Хранилище книг (в реальном проекте - база данных).
     * Используем ConcurrentMap для безопасности в многопоточной среде.
     * Key: id книги, Value: объект Book
     */
    private final ConcurrentMap<Long, Book> booksStore;

    /**
     * Counter для генерации уникальных ID.
     * Используем volatile для безопасности в многопоточной среде.
     */
    private volatile long idCounter;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию.
     * Создает пустое хранилище книг.
     */
    public BookDao() {
        this.booksStore = new ConcurrentHashMap<>();
        this.idCounter = 1;
    }

    /**
     * Конструктор с начальным набором книг.
     *
     * @param initialBooks список книг для добавления в хранилище
     */
    public BookDao(List<Book> initialBooks) {
        this.booksStore = new ConcurrentHashMap<>();
        this.idCounter = 1;

        // Добавление начальных книг
        if (initialBooks != null) {
            for (Book book : initialBooks) {
                save(book);
            }
        }
    }

    // ==================== МЕТОДЫ CRUD ====================

    /**
     * Метод для создания (создания) новой книги.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за создание книги
     *
     * @param book книга для сохранения (без id)
     * @return сохраненная книга с установленным id
     * @throws IllegalArgumentException если книга null
     */
    public Book create(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }

        // Генерация уникального ID
        Long newId = generateId();
        book.setId(newId);

        // Сохранение книги в хранилище
        booksStore.put(newId, book);

        return book;
    }

    /**
     * Метод для сохранения книги (create или update).
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за сохранение книги
     *
     * @param book книга для сохранения
     * @return сохраненная книга
     */
    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }

        // Если book имеет id - это update, иначе - create
        if (book.getId() == null) {
            return create(book);
        } else {
            return update(book);
        }
    }

    /**
     * Метод для обновления существующей книги.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за обновление книги
     *
     * @param book книга для обновления (с установленным id)
     * @return обновленная книга
     * @throws IllegalArgumentException если книга null или id не установлен
     * @throws RuntimeException         если книга с таким id не найдена
     */
    public Book update(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }

        if (book.getId() == null) {
            throw new IllegalArgumentException("ID книги не установлен для обновления");
        }

        // Проверка существования книги
        if (!booksStore.containsKey(book.getId())) {
            throw new RuntimeException(
                    "Книга с ID " + book.getId() + " не найдена"
            );
        }

        // Обновление книги в хранилище
        booksStore.put(book.getId(), book);

        return book;
    }

    /**
     * Метод для получения книги по ID.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за поиск книги по ID
     *
     * @param id идентификатор книги
     * @return Optional с книгой, если найдена, или пустой Optional
     */
    public Optional<Book> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(booksStore.get(id));
    }

    /**
     * Метод для получения всех книг.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за получение всех книг
     *
     * @return список всех книг (не null, может быть пустым)
     */
    public List<Book> findAll() {
        return new ArrayList<>(booksStore.values());
    }

    /**
     * Метод для поиска книг по названию (частичное совпадение).
     * <p>
     * ПРИНЦИП SOLID: Open/Closed Principle
     * - Метод открыт для расширения (можно добавить поиск по другим полям)
     *
     * @param title название книги для поиска (частичное совпадение)
     * @return список книг, соответствующих названию
     */
    public List<Book> findByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return findAll();
        }

        String searchTitle = title.trim().toLowerCase();
        List<Book> result = new ArrayList<>();

        for (Book book : booksStore.values()) {
            if (book.getTitle().toLowerCase().contains(searchTitle)) {
                result.add(book);
            }
        }

        return result;
    }

    /**
     * Метод для поиска книг по автору (полное совпадение).
     *
     * @param author имя автора для поиска
     * @return список книг автора
     */
    public List<Book> findByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return findAll();
        }

        String searchAuthor = author.trim().toLowerCase();
        List<Book> result = new ArrayList<>();

        for (Book book : booksStore.values()) {
            if (book.getAuthor().toLowerCase().equals(searchAuthor)) {
                result.add(book);
            }
        }

        return result;
    }

    /**
     * Метод для удаления книги по ID.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за удаление книги
     *
     * @param id идентификатор книги для удаления
     * @return true если книга удалена, false если не найдена
     */
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        return booksStore.remove(id) != null;
    }

    /**
     * Метод для проверки существования книги по ID.
     *
     * @param id идентификатор книги
     * @return true если книга существует, false иначе
     */
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }

        return booksStore.containsKey(id);
    }

    /**
     * Метод для получения числа всех книг.
     *
     * @return число книг в хранилище
     */
    public int count() {
        return booksStore.size();
    }

    /**
     * Метод для удаления всех книг.
     */
    public void deleteAll() {
        booksStore.clear();
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Метод для генерации уникального ID.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за генерацию ID
     *
     * @return уникальный идентификатор
     */
    private long generateId() {
        // Используем volatile для безопасности в многопоточной среде
        return idCounter++;
    }
}