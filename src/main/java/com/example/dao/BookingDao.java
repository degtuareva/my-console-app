package com.example.dao;

import com.example.model.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Класс BookingDao (Data Access Object для бронирования) - предоставляет доступ к данным бронирований.
 * <p>
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: управление данными бронирований
 *
 * @author Your Name
 * @version 1.0.0
 */
public class BookingDao {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /**
     * Хранилище бронирований
     */
    private final ConcurrentMap<Long, Booking> bookingsStore;

    /**
     * Counter для генерации уникальных ID
     */
    private volatile long idCounter;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию.
     */
    public BookingDao() {
        this.bookingsStore = new ConcurrentHashMap<>();
        this.idCounter = 1;
    }

    /**
     * Конструктор с начальным набором бронирований.
     *
     * @param initialBookings список бронирований для добавления
     */
    public BookingDao(List<Booking> initialBookings) {
        this.bookingsStore = new ConcurrentHashMap<>();
        this.idCounter = 1;

        if (initialBookings != null) {
            for (Booking booking : initialBookings) {
                save(booking);
            }
        }
    }

    // ==================== МЕТОДЫ CRUD ====================

    /**
     * Метод для создания нового бронирования.
     *
     * @param booking бронирование для сохранения
     * @return сохраненное бронирование с установленным id
     */
    public Booking create(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Бронирование не может быть null");
        }

        Long newId = generateId();
        booking.setId(newId);
        bookingsStore.put(newId, booking);

        return booking;
    }

    /**
     * Метод для сохранения бронирования (create или update).
     *
     * @param booking бронирование для сохранения
     * @return сохраненное бронирование
     */
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Бронирование не может быть null");
        }

        if (booking.getId() == null) {
            return create(booking);
        } else {
            return update(booking);
        }
    }

    /**
     * Метод для обновления существующего бронирования.
     *
     * @param booking бронирование для обновления
     * @return обновленное бронирование
     */
    public Booking update(Booking booking) {
        if (booking == null || booking.getId() == null) {
            throw new IllegalArgumentException("Бронирование и ID не могут быть null");
        }

        if (!bookingsStore.containsKey(booking.getId())) {
            throw new RuntimeException("Бронирование с ID " + booking.getId() + " не найдено");
        }

        bookingsStore.put(booking.getId(), booking);
        return booking;
    }

    /**
     * Метод для получения бронирования по ID.
     *
     * @param id идентификатор бронирования
     * @return Optional с бронированием, если найдено
     */
    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(id == null ? null : bookingsStore.get(id));
    }

    /**
     * Метод для получения всех бронирований.
     *
     * @return список всех бронирований
     */
    public List<Booking> findAll() {
        return new ArrayList<>(bookingsStore.values());
    }

    /**
     * Метод для получения активных бронирований (книги еще не возвращены).
     *
     * @return список активных бронирований
     */
    public List<Booking> findActive() {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookingsStore.values()) {
            if (booking.isActive()) {
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * Метод для получения бронирований по книге.
     *
     * @param bookId ID книги
     * @return список бронирований для этой книги
     */
    public List<Booking> findByBookId(Long bookId) {
        if (bookId == null) {
            return findAll();
        }

        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookingsStore.values()) {
            if (booking.getBook().getId() == bookId) {
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * Метод для получения бронирований по читателю.
     *
     * @param readerId ID читателя
     * @return список бронирований для этого читателя
     */
    public List<Booking> findByReaderId(Long readerId) {
        if (readerId == null) {
            return findAll();
        }

        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookingsStore.values()) {
            if (booking.getReader().getId() == readerId) {
                result.add(booking);
            }
        }
        return result;
    }

    /**
     * Метод для удаления бронирования по ID.
     *
     * @param id идентификатор бронирования
     * @return true если удалено, false если не найдено
     */
    public boolean deleteById(Long id) {
        return id != null && bookingsStore.remove(id) != null;
    }

    /**
     * Метод для проверки существования бронирования.
     *
     * @param id идентификатор бронирования
     * @return true если существует, false иначе
     */
    public boolean existsById(Long id) {
        return id != null && bookingsStore.containsKey(id);
    }

    /**
     * Метод для получения числа всех бронирований.
     *
     * @return число бронирований
     */
    public int count() {
        return bookingsStore.size();
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    /**
     * Метод для генерации уникального ID.
     *
     * @return уникальный идентификатор
     */
    private long generateId() {
        return idCounter++;
    }
}