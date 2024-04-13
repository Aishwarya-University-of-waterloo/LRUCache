package org.arctic.wolf;


public class Solution {
    public static void main(String[] args) throws InterruptedException {

        LFUCache<Integer,String> cache = new LFUCache<>();
        cache.initialize(5);
        cache.put(1,"A");
        cache.put(2,"B");
        cache.put(3,"C");
        //System.out.println(cache.leastRecentlyUsed.getKey()+ ": " + cache.leastRecentlyUsed.getValue() );
        //System.out.println(cache.mostRecentlyUsed.getKey()+ ": " + cache.mostRecentlyUsed.getValue() );
        Thread.sleep(10000);
        cache.put(4,"D");
        cache.put(5,"E");
        cache.put(6,"F");
        System.out.println(cache.leastRecentlyUsed.getKey()+ ": " + cache.leastRecentlyUsed.getValue() );
        System.out.println(cache.mostRecentlyUsed.getKey()+ ": " + cache.mostRecentlyUsed.getValue() );
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));

        System.out.println(cache.leastRecentlyUsed.getKey()+ ": " + cache.leastRecentlyUsed.getValue() );
        System.out.println(cache.mostRecentlyUsed.getKey()+ ": " + cache.mostRecentlyUsed.getValue() );
        System.out.println("hi");



    }


}