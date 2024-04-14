package org.arctic.wolf;
import javax.sound.midi.Soundbank;

import static org.junit.Assert.*;

public class Solution {
    public static void main(String[] args) throws InterruptedException {

        addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement();
        keysAndValuesAreGeneric();
        checkLeastRecent_And_MonstRecent_Value();
        addDataToCacheOfSizeLimit_WhenAddDataWithinSizeLimit_ThenDataShouldBeAccessible();
        updateSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement();
        evictLeastFrequentlyUsedData_WhenCacheIsFullAndNewDataAdded_ThenLeastFrequentDataShouldBeEvicted();
        invalidateCacheAfterSpecifiedTime_WhenDataAddedAndExpired_ThenDataShouldBeInvalidated();
        addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict();
        createCacheWithThread_ValidateCacheAfterExecutionOfBothThreads();
    }

    public static void addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement(){
        LFUCache<String,String> lfuCache = new LFUCache<>();
        lfuCache.initialize(lfuCache.maxSize= 5);
        lfuCache.put("1","test1");
        lfuCache.put("2","test2");
        lfuCache.put("3","test3");
        assertEquals("test1",lfuCache.get("1"));
        assertEquals("test2",lfuCache.get("2"));
        assertEquals("test3",lfuCache.get("3"));
        System.out.println("addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement :: Successful");
    }

    public static void keysAndValuesAreGeneric(){
        LFUCache<String,String> lfuCacheString = new LFUCache<>();
        lfuCacheString.initialize(3);
        lfuCacheString.put("one", "A");
        lfuCacheString.put("two", "B");
        lfuCacheString.put("three", "C");

        LFUCache<Integer,String> lfuCacheIntegerString = new LFUCache<>();
        lfuCacheIntegerString.initialize(3);
        lfuCacheIntegerString.put(1, "A");
        lfuCacheIntegerString.put(2, "B");
        lfuCacheIntegerString.put(3, "C");

        assertEquals(lfuCacheString.get("one"), "A");
        assertEquals(lfuCacheIntegerString.get(1), "A");
        System.out.println("keysAndValuesAreGeneric :: Successful");
    }

    public static void checkLeastRecent_And_MonstRecent_Value(){
        LFUCache<Integer,String> lfuCache = new LFUCache<>();
        lfuCache.initialize(5);
        lfuCache.put(1, "A");
        lfuCache.put(2, "B");
        lfuCache.put(3, "C");
        lfuCache.put(4, "D");
        lfuCache.put(5, "E");   // 5 is most recently added key and 1 is least recent
        lfuCache.get(1);  // here 1 is accessed again and so it becomes most recently used.
        assertTrue(lfuCache.mostRecentlyUsed.getKey().equals(1));
        assertTrue(lfuCache.leastRecentlyUsed.getKey().equals(2));

        System.out.println("checkLeastRecent_And_MonstRecent_Value :: Successful");
    }



    public static void evictLeastFrequentlyUsedData_WhenCacheIsFullAndNewDataAdded_ThenLeastFrequentDataShouldBeEvicted() {
        LFUCache<Integer, String> lfuCache = new LFUCache<>();
        lfuCache.initialize(3);
        lfuCache.put(1, "test1");
        lfuCache.put(2, "test2");
        lfuCache.put(3, "test3");
        lfuCache.get(1); // Accessing key 1 to increase its frequency
        lfuCache.get(2); // Accessing key 2 to increase its frequency
        lfuCache.put(4, "test4"); // Adding new data to evict least frequently used key
        assertTrue(lfuCache.get(3)==null); // Key 3 should be evicted
        System.out.println("evictLeastFrequentlyUsedData_WhenCacheIsFullAndNewDataAdded_ThenLeastFrequentDataShouldBeEvicted :: Successful");
    }


    public static void invalidateCacheAfterSpecifiedTime_WhenDataAddedAndExpired_ThenDataShouldBeInvalidated() throws InterruptedException {
        LFUCache<Integer, String> lfuCache = new LFUCache<>();
        lfuCache.initialize(3,1000); // TTL: 1 second
        lfuCache.put(1, "test1");
        Thread.sleep(1500); // Sleep for longer than TTL
        assertFalse(lfuCache.get(1)!=null);
        assertTrue(lfuCache.get(1)==null);
        System.out.println("invalidateCacheAfterSpecifiedTime_WhenDataAddedAndExpired_ThenDataShouldBeInvalidated :: Successful");
    }


    public static void addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict(){
        LFUCache<String,String> lfuCache = new LFUCache<>();//(3);
        lfuCache.initialize(3);
        lfuCache.put("1","test1");
        lfuCache.put("2","test2");
        lfuCache.put("3","test3");
        lfuCache.put("4","test4");
        assertTrue(lfuCache.get("1") == null);
        System.out.println("addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict :: Successful");
    }

    public  static void addDataToCacheOfSizeLimit_WhenAddDataWithinSizeLimit_ThenDataShouldBeAccessible() {
        LFUCache<Integer, String> lfuCache = new LFUCache<>();//(3);
        lfuCache.initialize(3);
        lfuCache.put(1, "test1");
        lfuCache.put(2, "test2");
        lfuCache.put(3, "test3");
        assertTrue(lfuCache.get(1) != null);
        assertTrue(lfuCache.get(2) != null);
        assertTrue(lfuCache.get(3) != null);
        System.out.println("addDataToCacheOfSizeLimit_WhenAddDataWithinSizeLimit_ThenDataShouldBeAccessible :: Successful");
    }

    public static void updateSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement(){
        LFUCache<String,String> lfuCache = new LFUCache<>();
        lfuCache.initialize(lfuCache.maxSize= 5);
        lfuCache.put("1","test1");
        lfuCache.put("2","test2");
        lfuCache.put("3","test3");
        lfuCache.put("1", "test4");
        assertEquals("test4",lfuCache.get("1"));
        assertNotEquals("test1", lfuCache.get("1"));
        System.out.println("updateSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement :: Successful");
    }

    public static void createCacheWithThread_ValidateCacheAfterExecutionOfBothThreads() throws InterruptedException {
        final LFUCache<Integer,String> cache = new LFUCache<>();
        cache.initialize(6, 120_000);
        Thread thread1 = new Thread(){
            @Override
            public  void run(){
                cache.put(1, "A");
                cache.put(2,"B");
                cache.put(1, "Z");
                cache.put(3, "C");

            }
        };

        Thread thread2 = new Thread(){
            @Override
            public  void run(){
                cache.put(11, "Welcome");
                cache.put(12, "To");
                cache.put(13, "Assignment");

            }
        };
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertTrue(cache.get(11).equals("Welcome") );
        assertTrue(cache.get(1).equals("Z"));
        System.out.println("createCacheWithThread_ValidateCacheAfterExecutionOfBothThreads :: Successful");
    }


}