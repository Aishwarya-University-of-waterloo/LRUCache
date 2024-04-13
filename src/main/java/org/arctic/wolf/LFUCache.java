package org.arctic.wolf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LFUCache<K, V> implements Cache<K, V>{
    ConcurrentHashMap<K, Node<K,V>> cacheMap;
    int maxSize;
    int currentSize;
    long totalTimeElapsed;
    long startTime;

    long timeToLive;
    Node<K,V> leastRecentlyUsed;

    Node<K,V> mostRecentlyUsed;

    @Override
    public void initialize(int maxSize, long timeToLive ) {
        this.cacheMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.currentSize=0;
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
        this.currentSize=0;
        this.leastRecentlyUsed = new Node<>(null,null,null,null);
        this.mostRecentlyUsed = leastRecentlyUsed;
        this.startTime = System.currentTimeMillis();
        this.totalTimeElapsed =0;
        this.timeToLive=100_000;
    }
    @Override
    public V get(K key) {

        if(!isCacheValid()){
            return null;
        }

        Node<K, V> node = cacheMap.getOrDefault(key,null);
        if(node == null){
            return null;
        }
        Node<K,V> nextNode = node.next;
        Node<K,V> prevNode = node.prev;


        // accessing most recently used node itself
        if(node.key == mostRecentlyUsed.getKey()){
            return mostRecentlyUsed.getValue();
        }
        // accessing least recently used node
        else if(node.key == leastRecentlyUsed.getKey()) {
            nextNode.prev = null;
            node.prev = mostRecentlyUsed;
            mostRecentlyUsed.next = node;
            mostRecentlyUsed = node;
            leastRecentlyUsed =  nextNode;
        }
        // accessing middle node
        else{
            prevNode.next=nextNode;
            nextNode.prev = prevNode;
            mostRecentlyUsed.next=node;
            node.prev = mostRecentlyUsed;
            mostRecentlyUsed=node;

        }
        mostRecentlyUsed.next=null;
        return node.getValue();
    }


    @Override
    public void put(K key, V value) {
        if(!isCacheValid()){
            System.out.println("Previous cache is invalid: creating new cache");
            this.initialize(this.maxSize, 10000);
            put(key,value);
        }

       if(cacheMap.containsKey(key)){
           return;
       }

       Node<K, V> node = new Node<K,V>(mostRecentlyUsed,null,key,value);
       mostRecentlyUsed.next = node;
       node.prev=mostRecentlyUsed;
       mostRecentlyUsed=node;
       cacheMap.put(key,node);

       if(currentSize == maxSize ){
           cacheMap.remove(leastRecentlyUsed.key);
           leastRecentlyUsed = leastRecentlyUsed.next;
           leastRecentlyUsed.prev = null;
       }

       else if(currentSize < maxSize){
           if(currentSize==0){
               leastRecentlyUsed = node;
           }
           currentSize++;

       }

    }



    @Override
    public void put(K key, V value, long timeToLive) {


    }



    @Override
    public boolean isCacheValid() {
        long timeElapsed = System.currentTimeMillis() - startTime;
        if(timeElapsed > timeToLive){
            cacheMap.clear();
            return false;
        }
       return true;
    }


}
