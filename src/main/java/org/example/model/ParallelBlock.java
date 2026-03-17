package org.example.model;

import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ParallelBlock implements SystemComponent {
    public List<SystemComponent> children;

    @Override
    public int evaluateLifetime(BigInteger mask) {
        int maxLifetime = -1;
        for (SystemComponent child : children) {
            maxLifetime = Math.max(maxLifetime, child.evaluateLifetime(mask));
        }
        return maxLifetime;
    }

    @Override
    public int calculateCost(BigInteger mask) {
        int totalCost = 0;
        for (SystemComponent child : children) {
            totalCost += child.calculateCost(mask);
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
