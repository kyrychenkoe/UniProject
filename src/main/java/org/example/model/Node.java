package org.example.model;

import org.example.components.SystemComponent;

import java.util.List;
import java.util.Set;

public class Node implements SystemComponent {
    public int id;
    public int baseLifetime;
    public int redundantLifetime;
    public int cost;

    @Override
    public int evaluateLifetime(Set<Integer> activeRedundancies) {
        return activeRedundancies.contains(id) ? baseLifetime + redundantLifetime : baseLifetime;
    }

    @Override
    public int calculateCost(Set<Integer> activeRedundancies) {
        return activeRedundancies.contains(id) ? cost : 0;
    }

    @Override
    public void extractNodes(List<Node> allNodes) {
        allNodes.add(this);
    }
}