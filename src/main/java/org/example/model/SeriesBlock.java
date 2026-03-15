package org.example.model;

import org.example.components.SystemComponent;

import java.util.List;
import java.util.Set;

public class SeriesBlock implements SystemComponent {
    public List<SystemComponent> children;

    @Override
    public int evaluateLifetime(Set<Integer> activeRedundancies) {
        int minLifetime = Integer.MAX_VALUE;
        for (SystemComponent child : children) {
            minLifetime = Math.min(minLifetime, child.evaluateLifetime(activeRedundancies));
        }
        return minLifetime;
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