package com.miniurl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ThreadUtil {

    public static final int NUMBER_OF_THREADS_PER_REQUEST = 50;

    public static ExecutorService getRequestExecutorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS_PER_REQUEST);
    }

    public static CompletableFuture<List<Object>> futureAll(CompletableFuture... futures) {

        if (futures == null || futures.length == 0)
            return null;

        List<CompletableFuture> allFuture = new ArrayList<>();

        for (int i = 0; i < futures.length; i++)
            allFuture.add(futures[i]);


        CompletableFuture<Void> allCompletableFuture = CompletableFuture
                .allOf(allFuture.toArray(new CompletableFuture[allFuture.size()]));

        CompletableFuture<List<Object>> allCompletableFutures = allCompletableFuture.thenApply(future -> {

            List<Object> futureList = new ArrayList<>();
            for (int i = 0; i < futures.length; i++)
                futureList.add(futures[i].join());
            return futureList;
        });

        allCompletableFutures.thenApply(futureList -> futureList.stream().collect(Collectors.toList()));

        return allCompletableFutures;
    }

    public static List<Object> awaitFutureAll(List<CompletableFuture> futures ){
        return Stream.of(futures.toArray(new CompletableFuture[futures.size()]))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static List<Object> awaitFutureAll(CompletableFuture... futures) {

        if (futures == null || futures.length == 0)
            return null;

        List<CompletableFuture> allFutures = new ArrayList<>();
        for (int i = 0; i < futures.length; i++)
            allFutures.add(futures[i]);
        return awaitFutureAll(allFutures);
    }

}
