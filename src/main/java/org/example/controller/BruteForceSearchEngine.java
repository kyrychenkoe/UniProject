package org.example.controller;

import lombok.Getter;
import org.example.components.SystemComponent;
import org.example.model.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
public class BruteForceSearchEngine implements SearchEngine {
    private final Set<Integer> bestCombination = new HashSet<>();
    private int bestLifetime = -1;
    private int bestCost = 0;
    private BigInteger bestCombinationRaw = BigInteger.ZERO;

    public void doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget) {
        this.bestLifetime = -1;
        this.bestCost = 0;
        this.bestCombinationRaw = BigInteger.ZERO;
        this.bestCombination.clear();

        int n = allNodes.size();
        var nodes = new ArrayList<>(allNodes);
        nodes.sort((node1, node2) -> Integer.compare(node2.getCost(), node1.getCost()));

        for (int i = 0; i < n; i++) {
            nodes.get(i).index = n - 1 - i;
        }

        BigInteger totalCombinations = BigInteger.TWO.pow(n);

        bruteForce(rootSystem, budget, totalCombinations);

        for (Node node : allNodes) {
            if (bestCombinationRaw.testBit(node.index)) {
                bestCombination.add(node.getId());
            }
        }
    }

    private void bruteForce(SystemComponent rootSystem, int budget, BigInteger totalCombinations) {
        for (BigInteger mask = BigInteger.ZERO; mask.compareTo(totalCombinations) < 0; mask = mask.add(BigInteger.ONE)) {

            int cost = rootSystem.calculateCost(mask);

            if (cost > budget) {
                int tz = mask.getLowestSetBit();
                if (tz == -1) {
                    break;
                }
                if (tz > 0) {
                    BigInteger skip = BigInteger.ONE.shiftLeft(tz).subtract(BigInteger.ONE);
                    mask = mask.or(skip);
                }
                continue;
            }
            int lifetime = rootSystem.evaluateLifetime(mask);
            if (lifetime > bestLifetime || (lifetime == bestLifetime && cost < bestCost)) {
                bestLifetime = lifetime;
                bestCost = cost;
                bestCombinationRaw = mask;
            }
        }
    }


    @Override
    public String getName() {
        return "BruteForceSearchEngine";
    }
}