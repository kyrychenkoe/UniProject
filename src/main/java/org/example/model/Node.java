package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Node implements SystemComponent {
    public int id;
    public int baseLifetime;
    public int redundantLifetime;
    public int cost;

    @JsonIgnore
    public int index;

    @Override
    public int evaluateLifetime(BigInteger mask) {
        return mask.testBit(index) ? baseLifetime + redundantLifetime : baseLifetime;
    }

    @Override
    public int calculateCost(BigInteger mask) {
        return mask.testBit(index) ? cost : 0;
    }

    @Override
    public void extractNodes(List<Node> allNodes) {
        allNodes.add(this);
    }
}