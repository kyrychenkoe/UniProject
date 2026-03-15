package org.example.controller;

import lombok.Getter;
import org.example.components.SystemComponent;
import org.example.model.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class BruteForceSearchEngine {
    private int bestLifetime = -1;
    private int bestCost = 0;
    private Set<Integer> bestCombination = new HashSet<>();

    public void doSearch(int nodeIndex, List<Node> allNodes,
                         Set<Integer> currentRedundancies,
                         SystemComponent rootSystem, int budget) {

        int cost = rootSystem.calculateCost(currentRedundancies);
        if (cost > budget) {
            return;
        }

        if (nodeIndex == allNodes.size()) {
            int lifetime = rootSystem.evaluateLifetime(currentRedundancies);
            if (lifetime > bestLifetime || (lifetime == bestLifetime && cost < bestCost)) {
                bestLifetime = lifetime;
                bestCost = cost;
                bestCombination = new HashSet<>(currentRedundancies);
            }
            return;
        }

        Node currentNode = allNodes.get(nodeIndex);

        doSearch(nodeIndex + 1, allNodes, currentRedundancies, rootSystem, budget);

        currentRedundancies.add(currentNode.id);
        doSearch(nodeIndex + 1, allNodes, currentRedundancies, rootSystem, budget);
        currentRedundancies.remove(currentNode.id);
    }
}
