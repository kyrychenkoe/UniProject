package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node implements SystemComponent {
    @JsonIgnore
    public int index;
    private int id;
    private double baseLifetime;
    private double redundantLifetime;
    private int cost;

    @Override
    public double evaluateLifetime(BigInteger mask) {
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

    @Override
    public SystemComponent deepCopy() {
        return new Node(-1, this.id, this.baseLifetime, this.redundantLifetime, this.cost);
    }

    public void randomizeLifetime() {
        this.baseLifetime = this.baseLifetime * Math.log(1.0 / (1.0 - Math.random()));
        this.redundantLifetime = this.redundantLifetime * Math.log(1.0 / (1.0 - Math.random()));
    }

}