package org.example.model;

import org.example.components.SystemComponent;

import java.util.List;
import java.util.Set;

public class ParallelBlock implements SystemComponent {
    public List<SystemComponent> children;

    @Override
    public int evaluateLifetime(Set<Integer> activeRedundancies) {
        int maxLifetime = -1;
        for (SystemComponent child : children) {
            maxLifetime = Math.max(maxLifetime, child.evaluateLifetime(activeRedundancies));
        }
        return maxLifetime;
    }

    @Override
    public int calculateCost(Set<Integer> activeRedundancies) {
        int totalCost = 0;
        for (SystemComponent child : children) {
            totalCost += child.calculateCost(activeRedundancies);
        }
        return totalCost;
    }

    @Override
    public void extractNodes(List<Node> allNodes) {
        for (SystemComponent child : children) {
            child.extractNodes(allNodes);
        }
    }
}
