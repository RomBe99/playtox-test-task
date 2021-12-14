package com.rombe.playtox.test.task.db;

public interface Database<K, V> {

    K create(K key, V value);

    V read(K key);

    V update(K key, V newValue);

    V delete(K key);

}
