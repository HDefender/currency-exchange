package dao;

import java.util.List;
import java.util.Optional;

public interface Dao <K> {
    List<K> findAll();
    Optional <K> create(K k);
}