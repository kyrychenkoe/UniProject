package org.example.controller;

import lombok.Getter;
import org.example.components.SystemComponent;
import org.example.model.Node;
import org.example.model.SearchResult;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

@Getter
public class RecursiveBruteForceSearchEngine implements SearchEngine {

    @Override
    public SearchResult doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget) {
        var result = new SearchResult();
        result.setRootSystem(rootSystem);

        for (int i = 0; i < allNodes.size(); i++) {
            allNodes.get(i).index = i;
        }

        doSearch(0, allNodes, BigInteger.ZERO, rootSystem, budget, result);

        var bestCombination = new HashSet<Integer>();
        for (Node node : allNodes) {
            if (result.getBestMask().testBit(node.index)) {
                bestCombination.add(node.getId());
            }
        }
        result.setBestCombination(bestCombination);

        return result;
    }

    @Override
    public String getName() {
        return "RecursiveBruteForceSearchEngine";
    }

    private void doSearch(int nodeIndex, List<Node> allNodes,
                          BigInteger currentMask,
                          SystemComponent rootSystem, int budget, SearchResult result) {
        int cost = rootSystem.calculateCost(currentMask);
        if (cost > budget) {
            return;
        }

        if (nodeIndex == allNodes.size()) {
            double lifetime = rootSystem.evaluateLifetime(currentMask);
            if (lifetime > result.getBestLifetime() || (lifetime == result.getBestLifetime() && cost < result.getBestCost())) {
                result.setBestLifetime(lifetime);
                result.setBestCost(cost);
                result.setBestMask(currentMask);
            }
            return;
        }

        Node currentNode = allNodes.get(nodeIndex);

        doSearch(nodeIndex + 1, allNodes, currentMask, rootSystem, budget, result);

        BigInteger withRedundancy = currentMask.setBit(currentNode.index);
        doSearch(nodeIndex + 1, allNodes, withRedundancy, rootSystem, budget, result);
    }
}