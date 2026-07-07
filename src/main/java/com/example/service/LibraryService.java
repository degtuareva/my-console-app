package com.example.service;

import com.example.dao.BookDao;
import com.example.dao.BookingDao;
import com.example.dao.ReaderDao;
import com.example.model.Book;
import com.example.model.Booking;
import com.example.model.Reader;

import java.util.List;
import java.util.Optional;

/**
 * Класс LibraryService - сервис для управления библиотекой (бизнес-логика).
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: реализация бизнес-логики библиотеки
 * - Не отвечает за хранение данных (DAO), не отвечает за отображение (CLI)
 * <p>
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс зависит от абстракций (DAO-интерфейсов), а не от конкретных реализаций
 * - В реальном проекте можно заменить DAO на работающие с PostgreSQL
 * <p>
 * ПРИНЦИП SOLID: Open/Closed Principle (OCP)
 * - Класс открыт для расширения (можно добавить новые методы)
 * - Класс закрыт для изменения (не нужно менять код при добавлении новых методов)
 * <p>
 * ПРИНЦИП SOLID: Interface Segregation Principle (ISP)
 * - Класс предоставляет только нужные методы для работы с библиотекой
 *
 * @author Your Name
 * @version 1.0.0
 */
public class LibraryService {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * DAO для работы с книгами
     */
    private final BookDao bookDao;

    /**
     * DAO для работы с читателями
     */
    private final ReaderDao readerDao;

    /**
     * DAO для работы с бронированиями
     */
    private final BookingDao bookingDao;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор с зависимостями (DAO).
     * <p>
     * ПРИНЦИП SOLID: Dependency Inversion Principle
     * - Зависимости передаются через конструктор (Dependency Injection)
     * - Это позволяет легко заменять DAO на mock-объекты для тестирования
     *
     * @param bookDao    DAO для книг
     * @param readerDao  DAO для читателей
     * @param bookingDao DAO для бронирований
     */
    public LibraryService(BookDao bookDao, ReaderDao readerDao, BookingDao bookingDao) {
        if (bookDao == null) {
            throw new IllegalArgumentException("BookDao не может быть null");
        }
        if (readerDao == null) {
            throw new IllegalArgumentException("ReaderDao не может быть null");
        }
        if (bookingDao == null) {
            throw new IllegalArgumentException("BookingDao не может быть null");
        }

        this.bookDao = bookDao;
        this.readerDao = readerDao;
        this.bookingDao = bookingDao;
    }

    // ==================== МЕТОДЫ ДЛЯ РАБОТЫ С КНИГАМИ ====================

    /**
     * Метод для добавления новой книги в библиотеку.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за добавление книги
     *
     * @param title  название книги
     * @param author имя автора
     * @param year   год издания
     * @param isbn   ISBN книги
     * @return добавленная книга с установленным ID
     */
    public Book addBook(String title, String author, Integer year, String isbn) {
        // Создание объекта книги
        Book book = new Book(title, author, year, isbn);

        // Сохранение книги через DAO
        return bookDao.create(book);
    }

    /**
     * Метод для получения книги по ID.
     *
     * @param id идентификатор книги
     * @return Optional с книгой, если найдена
     */
    public Optional<Book> getBookById(Long id) {
        return bookDao.findById(id);
    }

    /**
     * Метод для получения всех книг.
     *
     * @return список всех книг
     */
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    /**
     * Метод для поиска книг по названию.
     *
     * @param title название книги для поиска
     * @return список книг, соответствующих названию
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    /**
     * Метод для поиска книг по автору.
     *
     * @param author имя автора для поиска
     * @return список книг автора
     */
    public List<Book> searchBooksByAuthor(String author) {
        return bookDao.findByAuthor(author);
    }

    /**
     * Метод для обновления книги.
     *
     * @param book книга для обновления (с установленным ID)
     * @return обновленная книга
     */
    public Book updateBook(Book book) {
        return bookDao.update(book);
    }

    /**
     * Метод для удаления книги по ID.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за удаление книги
     * - Также удаляет все бронирования для этой книги (связанные данные)
     *
     * @param id идентификатор книги
     * @return true если книга удалена, false если не найдена
     */
    public boolean deleteBook(Long id) {
        // Сначала удаляем все бронирования для этой книги
        List<Booking> bookBookings = bookingDao.findByBookId(id);
        for (Booking booking : bookBookings) {
            bookingDao.deleteById(booking.getId());
        }

        // Затем удаляем книгу
        return bookDao.deleteById(id);
    }

    /**
     * Метод для проверки, доступна ли книга для бронирования.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за проверку доступности книги
     *
     * @param bookId ID книги
     * @return true если книга доступна (нет активных бронирований), false иначе
     */
    public boolean isBookAvailable(Long bookId) {
        List<Booking> activeBookings = bookingDao.findByBookId(bookId);

        // Проверка, есть ли активные бронирования
        for (Booking booking : activeBookings) {
            if (booking.isActive()) {
                return false;
            }
        }

        return true;
    }

    // ==================== МЕТОДЫ ДЛЯ РАБОТЫ С ЧИТАТЕЛЯМИ ====================

    /**
     * Метод для добавления нового читателя в библиотеку.
     *
     * @param name  имя читателя
     * @param phone телефон читателя
     * @return добавленный читатель с установленным ID
     */
    public Reader addReader(String name, String phone) {
        // Создание объекта читателя
        Reader reader = new Reader(name, phone);

        // Сохранение читателя через DAO
        return readerDao.create(reader);
    }

    /**
     * Метод для получения читателя по ID.
     *
     * @param id идентификатор читателя
     * @return Optional с читателем, если найден
     */
    public Optional<Reader> getReaderById(Long id) {
        return readerDao.findById(id);
    }

    /**
     * Метод для получения всех читателей.
     *
     * @return список всех читателей
     */
    public List<Reader> getAllReaders() {
        return readerDao.findAll();
    }

    /**
     * Метод для поиска читателей по имени.
     *
     * @param name имя читателя для поиска
     * @return список читателей с таким именем
     */
    public List<Reader> searchReadersByName(String name) {
        return readerDao.findBy_name(name);
    }

    /**
     * Метод для обновления читателя.
     *
     * @param reader читатель для обновления
     * @return обновленный читатель
     */
    public Reader updateReader(Reader reader) {
        return readerDao.update(reader);
    }

    /**
     * Метод для удаления читателя по ID.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за удаление читателя
     * - Также удаляет все бронирования для этого читателя
     *
     * @param id идентификатор читателя
     * @return true если читатель удален, false если не найден
     */
    public boolean deleteReader(Long id) {
        // Сначала удаляем все бронирования для этого читателя
        List<Booking> readerBookings = bookingDao.findByReaderId(id);
        for (Booking booking : readerBookings) {
            bookingDao.deleteById(booking.getId());
        }

        // Затем удаляем читателя
        return readerDao.deleteById(id);
    }

    // ==================== МЕТОДЫ ДЛЯ РАБОТЫ С БРОНИРОВАНИЯМИ ====================

    /**
     * Метод для бронирования книги читателем.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за бронирование книги
     * - Включает проверку доступности книги
     * <p>
     * ПРИНЦИП SOLID: Open/Closed Principle
     * - Метод открыт для расширения (можно добавить проверку лимитов бронирования)
     *
     * @param bookId   ID книги для бронирования
     * @param readerId ID читателя, который бронировал
     * @return созданное бронирование
     * @throws RuntimeException если книга не найдена, читатель не найден или книга недоступна
     */
    public Booking bookBook(Long bookId, Long readerId) {
        // Проверка существования книги
        Optional<Book> bookOptional = bookDao.findById(bookId);
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Книга с ID " + bookId + " не найдена");
        }
        Book book = bookOptional.get();

        // Проверка существования читателя
        Optional<Reader> readerOptional = readerDao.findById(readerId);
        if (!readerOptional.isPresent()) {
            throw new RuntimeException("Читатель с ID " + readerId + " не найден");
        }
        Reader reader = readerOptional.get();

        // Проверка доступности книги
        if (!isBookAvailable(bookId)) {
            throw new RuntimeException(
                    "Книга \"" + book.getTitle() + "\" недоступна для бронирования (уже забронирована)"
            );
        }

        // Создание бронирования
        Booking booking = new Booking(book, reader);

        // Сохранение бронирования через DAO
        return bookingDao.create(booking);
    }

    /**
     * Метод для возврата книги читателем.
     * <p>
     * ПРИНЦИП SOLID: Single Responsibility Principle
     * - Метод отвечает только за возврат книги
     *
     * @param bookingId ID бронирования
     * @return true если книга успешно возвращена, false если бронирование не найдено или книга уже возвращена
     */
    public boolean returnBook(Long bookingId) {
        // Проверка существования бронирования
        Optional<Booking> bookingOptional = bookingDao.findById(bookingId);
        if (!bookingOptional.isPresent()) {
            return false;
        }

        Booking booking = bookingOptional.get();

        // Проверка, что книга еще не возвращена
        if (!booking.isActive()) {
            return false;
        }

        // Возврат книги
        return booking.returnBook();
    }

    /**
     * Метод для получения бронирования по ID.
     *
     * @param id идентификатор бронирования
     * @return Optional с бронированием, если найдено
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingDao.findById(id);
    }

    /**
     * Метод для получения всех бронирований.
     *
     * @return список всех бронирований
     */
    public List<Booking> getAllBookings() {
        return bookingDao.findAll();
    }

    /**
     * Метод для получения активных бронирований.
     *
     * @return список активных бронирований
     */
    public List<Booking> getActiveBookings() {
        return bookingDao.findActive();
    }

    // ==================== МЕТОДЫ ДЛЯ СТАТИСТИКИ ====================

    /**
     * Метод для получения числа всех книг в библиотеке.
     *
     * @return число книг
     */
    public int getBookCount() {
        return bookDao.count();
    }

    /**
     * Метод для получения числа всех читателей.
     *
     * @return число читателей
     */
    public int getReaderCount() {
        return readerDao.count();
    }

    /**
     * Метод для получения числа активных бронирований.
     *
     * @return число активных бронирований
     */
    public int getActiveBookingCount() {
        return bookingDao.findActive().size();
    }
}