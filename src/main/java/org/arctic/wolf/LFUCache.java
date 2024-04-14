package org.arctic.wolf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LFUCache<K, V> implements Cache<K, V>{
    ConcurrentHashMap<K, Node<K,V>> cacheMap;
    int maxSize;
    AtomicInteger currentSize;
    long totalTimeElapsed;
    long startTime;
    long timeToLive;
    Node<K,V> leastRecentlyUsed;
    Node<K,V> mostRecentlyUsed;

    @Override
    public void initialize(int maxSize, long timeToLive ) {
        this.cacheMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.currentSize = new AtomicInteger(0);
        this.leastRecentlyUsed = new Node<>(null,null,null,null);
        this.mostRecentlyUsed = leastRecentlyUsed;
        this.startTime = System.currentTimeMillis();
        this.totalTimeElapsed =0;
        this.timeToLive=timeToLive;
    }


    @Override
    public void initialize(int maxSize) {
        this.cacheMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.currentSize = new AtomicInteger(0);
        this.leastRecentlyUsed = new Node<>(null,null,null,null);
        this.mostRecentlyUsed = leastRecentlyUsed;
        this.startTime = System.currentTimeMillis();
        this.totalTimeElapsed = 0;
        this.timeToLive=100_000;
    }



    @Override
    public V get(K key) {

        synchronized (this) {
            if (!isCacheValid()) {
                return null;
            }

            Node<K, V> node = cacheMap.getOrDefault(key, null);
            if (node == null) {
                return null;
            }
            Node<K, V> nextNode = node.next;
            Node<K, V> prevNode = node.prev;


            // accessing most recently used node itself
            if (node.getKey() == mostRecentlyUsed.getKey()) {
                return mostRecentlyUsed.getValue();
            }
            // accessing least recently used node
            else if (node.getKey() == leastRecentlyUsed.getKey()) {
                nextNode.prev = null;
                node.prev = mostRecentlyUsed;
                mostRecentlyUsed.next = node;
                mostRecentlyUsed = node;
                leastRecentlyUsed = nextNode;
            }
            // accessing middle node
            else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                mostRecentlyUsed.next = node;
                node.prev = mostRecentlyUsed;
                mostRecentlyUsed = node;

            }
            mostRecentlyUsed.next = null;
            return node.getValue();
        }
    }


    @Override
    public void put(K key, V value) {
        synchronized (this)
        {
            if (!isCacheValid()) {
                System.out.println("Previous cache is invalid: creating new cache");
                this.initialize(this.maxSize, 10000);
                put(key, value);
            }

            Node<K, V> node = cacheMap.getOrDefault(key, null);
            if(node != null){
             deleteNode(node);
            }
            node = new Node<K, V>(mostRecentlyUsed, null, key, value);
            addNode(node, key, true);

        }

    }

   public void addNode(Node<K, V> node,K key, boolean isNewNode){
        mostRecentlyUsed.next = node;
        node.prev = mostRecentlyUsed;
        mostRecentlyUsed = node;
        cacheMap.put(key, node);

        if (currentSize.intValue() == maxSize) {
            cacheMap.remove(leastRecentlyUsed.getKey());
            leastRecentlyUsed = leastRecentlyUsed.next;
            leastRecentlyUsed.prev = null;
        } else if (currentSize.intValue() < maxSize) {
            if (currentSize.intValue() == 0) {
                leastRecentlyUsed = node;
            }
            if(isNewNode)
                currentSize.addAndGet(1);

        }
    }

    public void deleteNode(Node<K,V> node){
        if(leastRecentlyUsed == mostRecentlyUsed){
            cacheMap.clear();
            return;
        }

        Node<K, V> nextNode = node.next;
        Node<K, V> prevNode = node.prev;

        if(leastRecentlyUsed.getKey() == node.getKey()){
            leastRecentlyUsed = nextNode;
            nextNode.prev = null;
        }
        else if(mostRecentlyUsed.getKey() == node.getKey()){
            mostRecentlyUsed = prevNode;
            prevNode.next = null;
        }
        else{
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        cacheMap.remove(node.getKey());
        currentSize.addAndGet(-1);
    }

    public boolean isCacheValid() {
        long timeElapsed = System.currentTimeMillis() - startTime;
        if(timeElapsed > timeToLive){
            cacheMap.clear();
            return false;
        }
       return true;
    }


}
