package org.example.controller;

import lombok.Getter;
import org.example.components.SystemComponent;
import org.example.model.Node;
import org.example.model.SearchResult;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Getter
public class BruteForceSearchEngine implements SearchEngine {

    public SearchResult doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget) {

        SearchResult result = new SearchResult();
        result.setRootSystem(rootSystem);

        int n = allNodes.size();
        var nodes = new ArrayList<>(allNodes);
        // nodes.sort((node1, node2) -> Integer.compare(node2.getCost(), node1.getCost()));

        for (int i = 0; i < n; i++) {
            nodes.get(i).index = n - 1 - i;
        }

        BigInteger totalCombinations = BigInteger.TWO.pow(n);

        var bestCombinationRaw = bruteForce(rootSystem, budget, totalCombinations, result);


        var bestCombination = new HashSet<Integer>();
        for (Node node : allNodes) {
            if (bestCombinationRaw.testBit(node.index)) {
                bestCombination.add(node.getId());
            }
        }
        result.setBestCombination(bestCombination);
        return result;
    }

    private BigInteger bruteForce(SystemComponent rootSystem, int budget, BigInteger totalCombinations, SearchResult result) {
        var bestCombinationRaw = BigInteger.ZERO;

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
            double lifetime = rootSystem.evaluateLifetime(mask);
            if (lifetime > result.getBestLifetime() || (lifetime == result.getBestLifetime() && cost < result.getBestCost())) {
                result.setBestCost(cost);
                result.setBestLifetime(lifetime);
                bestCombinationRaw = mask;
            }
        }

        return bestCombinationRaw;
    }


    @Override
    public String getName() {
        return "BruteForceSearchEngine";
    }
}