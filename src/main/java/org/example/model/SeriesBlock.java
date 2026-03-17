package org.example.model;

import lombok.Setter;
import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Setter
public class SeriesBlock implements SystemComponent {
    private List<SystemComponent> children;

    @Override
    public double evaluateLifetime(BigInteger mask) {
        double minLifetime = Integer.MAX_VALUE;
        for (SystemComponent child : children) {
            minLifetime = Math.min(minLifetime, child.evaluateLifetime(mask));
        }
        return minLifetime;
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

    @Override
    public SystemComponent deepCopy() {
        SeriesBlock copy = new SeriesBlock();
        copy.children = new ArrayList<>();
        for (SystemComponent child : this.children) {
            copy.children.add(child.deepCopy());
        }
        return copy;
    }
}