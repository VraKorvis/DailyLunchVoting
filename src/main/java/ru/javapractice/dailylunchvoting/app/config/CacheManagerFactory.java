package ru.javapractice.dailylunchvoting.app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static ru.javapractice.dailylunchvoting.app.config.CacheNames.*;
import static ru.javapractice.dailylunchvoting.app.config.CacheNames.TODAY_MENUS;

@UtilityClass
@Slf4j
public class CacheManagerFactory {

    public static CacheManager createDefault() {
        return new ConcurrentMapCacheManager();
    }

    public static CacheManager createCaffeine() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(getCacheNames());

        cacheManager.registerCustomCache(USER_CACHE, Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build());

        cacheManager.registerCustomCache(USERS_LIST, Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build());

        cacheManager.registerCustomCache(USERS_PAGE, Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build());

        var restaurantCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1);

        cacheManager.registerCustomCache(RESTAURANT, restaurantCacheBuilder.build());
        cacheManager.registerCustomCache(RESTAURANT_LIST, restaurantCacheBuilder.build());
        cacheManager.registerCustomCache(RESTAURANTS_WITH_TODAY_MENU_LIST, restaurantCacheBuilder.build());
        cacheManager.registerCustomCache(RESTAURANTS_WITH_TODAY_MENU_PAGE, Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build());

        var menuItemCacheBuilder = Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterAccess(15, TimeUnit.MINUTES);

        cacheManager.registerCustomCache(MENU_ITEMS_LIST, menuItemCacheBuilder.build());
        cacheManager.registerCustomCache(MENU_ITEM, menuItemCacheBuilder.build());
        cacheManager.registerCustomCache(MENU_ITEMS_MAP, Caffeine.newBuilder()
                .maximumSize(1)
                .build());
        cacheManager.registerCustomCache(MENU_ITEMS_PAGE, Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build());

        log.info("[CACHE] >>> Available cache names: {}", Arrays.toString(cacheManager.getCacheNames().toArray()));
        return cacheManager;
    }

    private static String[] getCacheNames() {
        return new String[]{
                USER_CACHE,
                USERS_PAGE,
                USERS_LIST,

                RESTAURANT,
                RESTAURANT_LIST,
                RESTAURANTS_WITH_TODAY_MENU_LIST,
                RESTAURANTS_WITH_TODAY_MENU_PAGE,
                TODAY_MENUS,

                MENU_ITEMS_LIST,
                MENU_ITEMS_PAGE,
                MENU_ITEMS_MAP,
                MENU_ITEM
        };
    }
}
