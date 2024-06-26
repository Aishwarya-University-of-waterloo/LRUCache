package org.arctic.wolf;

public interface Cache<K,V> {
    public void initialize(int size);
    public void initialize(int size, long timeToLive);
    public void put(K key, V value);
    public V get(K key);
}
