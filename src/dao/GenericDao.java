package dao;

import java.util.List;

public interface GenericDao<T> {
    void add(T entity);
    T findById(int id);
    List<T> findAll();
    void update(T entity);
    void delete(int id);
}
