package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.components.SystemComponent;
import org.example.controller.BruteForceSearchEngine;
import org.example.controller.RecursiveBruteForceSearchEngine;
import org.example.controller.SearchEngine;
import org.example.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {



    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        int budget = 50;

        var engines = List.of(
                new RecursiveBruteForceSearchEngine(),
                new BruteForceSearchEngine()
        );

        for (SearchEngine engine : engines) {
            try {
                SystemComponent rootSystem = mapper.readValue(new File("topology.json"), SystemComponent.class);

                List<Node> allNodes = new ArrayList<>();
                rootSystem.extractNodes(allNodes);

                System.out.println("Знайдено кінцевих вузлів: " + allNodes.size());
                var start = System.currentTimeMillis();
                engine.doSearch(allNodes, rootSystem, budget);
                var end = System.currentTimeMillis();

                System.out.println("--- РІШЕННЯ ---");
                System.out.println("Час життя: " + engine.getBestLifetime());
                System.out.println("Витрачено: " + engine.getBestCost() + " / " + budget);
                System.out.println("Резерви куплені для вузлів з ID: " + engine.getBestCombination());
                System.out.println("Пошуковий алгоритм: " + engine.getName());
                System.out.println("Час виконання: " + (end - start) + " мс");


            } catch (IOException e) {
                System.err.println("Помилка читання JSON: " + e.getMessage());
            }
        }

    }
}