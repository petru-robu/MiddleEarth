package com.middleearth;

import java.util.List;

public interface GenericRepository<T> {
    void insert(T object);
    T getById(int id);
    List<T> getAll();
    void update(T object);
    void delete(int id);
}
