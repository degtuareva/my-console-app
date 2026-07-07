package com.example.dao;

import com.example.model.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Класс ReaderDao (Data Access Object для читателя) - предоставляет доступ к данным читателей.
 *
 * ПРИНЦИП SOLID: Single Responsibility Principle (SRP)
 * - Класс имеет единственную ответственность: управление данными читателей
 *
 * ПРИНЦИП SOLID: Dependency Inversion Principle (DIP)
 * - Класс работает с абстракциями (Reader), а не с конкретными реализациями БД
 *
 * @author Your Name
 * @version 1.0.0
 */
public class ReaderDao {

    // ==================== ПОЛЯ (ATTRIBUTES) ====================

    /** Хранилище читателей */
    private final ConcurrentMap<Long, Reader> readersStore;

    /** Counter для генерации уникальных ID */
    private volatile long idCounter;

    // ==================== КОНСТРУКТОРЫ ====================

    /**
     * Конструктор по умолчанию.
     */
    public ReaderDao() {
        this.readersStore = new ConcurrentHashMap<>();
        this.idCounter = 1;
    }

    /**
     * Конструктор с начальным набором читателей.
     *
     * @param initialReaders список читателей для добавления
     */
    public ReaderDao(List<Reader> initialReaders) {
        this.readersStore = new ConcurrentHashMap<>();
        this.idCounter = 1;

        if (initialReaders != null) {
            for (Reader reader : initialReaders) {
                save(reader);
            }
        }
    }

    // ==================== МЕТОДЫ CRUD ====================

    /**
     * Метод для создания нового читателя.
     *
     * @param reader читатель для сохранения
     * @return сохраненный читатель с установленным id
     */
    public Reader create(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Читатель не может быть null");
        }

        Long newId = generateId();
        reader.setId(newId);
        readersStore.put(newId, reader);

        return reader;
    }

    /**
     * Метод для сохранения читателя (create или update).
     *
     * @param reader читатель для сохранения
     * @return сохраненный читатель
     */
    public Reader save(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Читатель не может быть null");
        }

        if (reader.getId() == null) {
            return create(reader);
        } else {
            return update(reader);
        }
    }

    /**
     * Метод для обновления существующего читателя.
     *
     * @param reader читатель для обновления
     * @return обновленный читатель
     * @throws RuntimeException если читатель не найден
     */
    public Reader update(Reader reader) {
        if (reader == null || reader.getId() == null) {
            throw new IllegalArgumentException("Читатель и ID не могут быть null");
        }

        if (!readersStore.containsKey(reader.getId())) {
            throw new RuntimeException("Читатель с ID " + reader.getId() + " не найден");
        }

        readersStore.put(reader.getId(), reader);
        return reader;
    }

    /**
     * Метод для получения читателя по ID.
     *
     * @param id идентификатор читателя
     * @return Optional с читателем, если найден
     */
    public Optional<Reader> findById(Long id) {
        return Optional.ofNullable(id == null ? null : readersStore.get(id));
    }

    /**
     * Метод для получения всех читателей.
     *
     * @return список всех читателей
     */
    public List<Reader> findAll() {
        return new ArrayList<>(readersStore.values());
    }

    /**
     * Метод для поиска читателей по имени.
     *
     * @param name имя читателя для поиска
     * @return список читателей с таким именем
     */
    public List<Reader> findBy_name(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }

        String searchName = name.trim().toLowerCase();
        List<Reader> result = new ArrayList<>();

        for (Reader reader : readersStore.values()) {
            if (reader.getName().toLowerCase().contains(searchName)) {
                result.add(reader);
            }
        }

        return result;
    }

    /**
     * Метод для удаления читателя по ID.
     *
     * @param id идентификатор читателя
     * @return true если удален, false если не найден
     */
    public boolean deleteById(Long id) {
        return id != null && readersStore.remove(id) != null;
    }

    /**
     * Метод для проверки существования читателя.
     *
     * @param id идентификатор читателя
     * @return true если существует, false иначе
     */
    public boolean existsById(Long id) {
        return id != null && readersStore.containsKey(id);
    }

    /**
     * Метод для получения числа всех читателей.
     *
     * @return число читателей
     */
    public int count() {
        return readersStore.size();
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