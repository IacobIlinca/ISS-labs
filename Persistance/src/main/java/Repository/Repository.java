package Repository;

import java.util.Collection;
import java.util.List;

public interface Repository<T, Tid> {
    boolean add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    List<T> findAll();
    Collection<T> getAll();

}
