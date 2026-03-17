package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.components.SystemComponent;
import org.example.controller.BruteForceSearchEngine;
import org.example.controller.RecursiveBruteForceSearchEngine;
import org.example.controller.SearchEngine;
import org.example.model.Node;
import org.example.model.SearchResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        int budget = 150;
        int iterCount = 100;
        boolean parallelEnabled = true;
        boolean sequentialEnabled = false;

        var engines = List.of(
                //    new RecursiveBruteForceSearchEngine(),
                new BruteForceSearchEngine()
        );

        for (SearchEngine engine : engines) {
            try {
                SystemComponent rootSystem = mapper.readValue(new File("topology.json"), SystemComponent.class);

                List<Node> allNodes = new ArrayList<>();
                rootSystem.extractNodes(allNodes);

                System.out.println("Знайдено кінцевих вузлів: " + allNodes.size());
                var start = System.currentTimeMillis();
                var searchResult = engine.doSearch(allNodes, rootSystem, budget);
                var end = System.currentTimeMillis();

                System.out.println("--- РІШЕННЯ ---");
                System.out.println("Час життя: " + searchResult.getBestLifetime());
                System.out.println("Витрачено: " + searchResult.getBestCost() + " / " + budget);
                System.out.println("Резерви куплені для вузлів з ID: " + searchResult.getBestCombination());
                System.out.println("Пошуковий алгоритм: " + engine.getName());
                System.out.println("Час виконання: " + (end - start) + " мс");

                if(parallelEnabled) {
                    System.out.println("--- РІШЕННЯ З РАНДОМІЗАЦІЄЮ (паралельне обчислення)---");
                    start = System.currentTimeMillis();
                    var deepSearchResult = engine.doSearchWithRandomizationParallel(rootSystem, budget, iterCount);
                    end = System.currentTimeMillis();
                    double averageLifetime = deepSearchResult.stream()
                            .map(SearchResult::getBestLifetime)
                            .mapToDouble(Double::doubleValue)
                            .average().orElse(0);
                    System.out.println("Середній час життя з рандомізацією: " + averageLifetime);
                    System.out.println("Час виконання: " + (end - start) + " мс");
                }

                if(sequentialEnabled) {
                    System.out.println("--- РІШЕННЯ З РАНДОМІЗАЦІЄЮ (послідовне обчислення)---");
                    start = System.currentTimeMillis();
                    var deepSearchResult = engine.doSearchWithRandomizationSequential(rootSystem, budget, iterCount);
                    end = System.currentTimeMillis();
                    var averageLifetime = deepSearchResult.stream()
                            .map(SearchResult::getBestLifetime)
                            .mapToDouble(Double::doubleValue)
                            .average().orElse(0);
                    System.out.println("Середній час життя з рандомізацією: " + averageLifetime);
                    System.out.println("Час виконання: " + (end - start) + " мс");
                }

                System.out.println("-----------------------------");
                System.out.println();


            } catch (IOException e) {
                System.err.println("Помилка читання JSON: " + e.getMessage());
            }
        }

    }
}