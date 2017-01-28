package com.slab.imageloaderlib;


public class Cache {
    /**
     * MAX_SIZE is  cache size
     */
    public static int MAX_SIZE = 10;
    /**
     * memoryCacheStorage is able to store any type of data i.e. Bitmap,JSON,XML,etc
     */
    public static MaxSizeHashMap memoryCacheStorage = new MaxSizeHashMap(MAX_SIZE);
}
