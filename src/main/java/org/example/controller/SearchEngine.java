package org.example.controller;

import lombok.SneakyThrows;
import org.example.components.SystemComponent;
import org.example.model.Node;
import org.example.model.SearchResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface SearchEngine {
    SearchResult doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget);

    String getName();

    @SneakyThrows
    default List<SearchResult> doSearchWithRandomizationSequential(SystemComponent rootSystem, int budget, int iterations) {
        List<SearchResult> results = new LinkedList<>();

        for (int i = 0; i < iterations; i++) {
            var result = getSearchResult(rootSystem, budget);
            results.add(result);
        }

        return results;
    }

    @SneakyThrows
    default List<SearchResult> doSearchWithRandomizationParallel(SystemComponent rootSystem, int budget, int iterations) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        try {
            List<Future<SearchResult>> futures = new LinkedList<>();

            for (int i = 0; i < iterations; i++) {
                Future<SearchResult> future = executor.submit(() -> getSearchResult(rootSystem, budget));
                futures.add(future);
            }

            List<SearchResult> results = new LinkedList<>();
            for (Future<SearchResult> future : futures) {
                results.add(future.get(1, TimeUnit.MINUTES));
            }
            executor.shutdown();
            return results;
        } finally {
            executor.shutdownNow();
        }
    }

    private SearchResult getSearchResult(SystemComponent rootSystem, int budget) {
        List<Node> allNodes = new ArrayList<>();
        var root = rootSystem.deepCopy();
        root.extractNodes(allNodes);
        for (Node node : allNodes) {
            node.randomizeLifetime();
        }
        return doSearch(allNodes, root, budget);
    }
}
