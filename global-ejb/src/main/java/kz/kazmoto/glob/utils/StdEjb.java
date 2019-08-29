package kz.kazmoto.glob.utils;

public interface StdEjb<T> {
    T findById(Long id);
    T create(T entity);
    T update(T entity);
}
