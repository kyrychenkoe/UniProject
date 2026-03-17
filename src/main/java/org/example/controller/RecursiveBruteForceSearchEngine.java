package org.example.controller;

import lombok.Getter;
import org.example.components.SystemComponent;
import org.example.model.Node;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class RecursiveBruteForceSearchEngine implements SearchEngine {
    private int bestLifetime = -1;
    private int bestCost = 0;
    private BigInteger bestCombinationRaw = BigInteger.ZERO;
    private final Set<Integer> bestCombination = new HashSet<>();

    @Override
    public void doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget) {
        this.bestLifetime = -1;
        this.bestCost = 0;
        this.bestCombinationRaw = BigInteger.ZERO;
        this.bestCombination.clear();

        for (int i = 0; i < allNodes.size(); i++) {
            allNodes.get(i).index = i;
        }

        doSearch(0, allNodes, BigInteger.ZERO, rootSystem, budget);

        if (bestLifetime != -1) {
            for (Node node : allNodes) {
                if (bestCombinationRaw.testBit(node.index)) {
                    bestCombination.add(node.id);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "RecursiveBruteForceSearchEngine";
    }

    private void doSearch(int nodeIndex, List<Node> allNodes,
                          BigInteger currentMask,
                          SystemComponent rootSystem, int budget) {

        int cost = rootSystem.calculateCost(currentMask);
        if (cost > budget) {
            return;
        }

        if (nodeIndex == allNodes.size()) {
            int lifetime = rootSystem.evaluateLifetime(currentMask);
            if (lifetime > bestLifetime || (lifetime == bestLifetime && cost < bestCost)) {
                bestLifetime = lifetime;
                bestCost = cost;
                bestCombinationRaw = currentMask; // Зберігаємо маску
            }
            return;
        }

        Node currentNode = allNodes.get(nodeIndex);

        doSearch(nodeIndex + 1, allNodes, currentMask, rootSystem, budget);

        BigInteger withRedundancy = currentMask.setBit(currentNode.index);
        doSearch(nodeIndex + 1, allNodes, withRedundancy, rootSystem, budget);
    }
}