package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.List;

@Data
public class Node implements SystemComponent {
    @JsonIgnore
    public int index;
    private int id;
    private int baseLifetime;
    private int redundantLifetime;
    private int cost;

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