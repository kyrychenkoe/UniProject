package org.example.model;

import lombok.Data;
import org.example.components.SystemComponent;

import java.math.BigInteger;
import java.util.Set;

@Data
public class SearchResult {
    private Set<Integer> bestCombination;
    private double bestLifetime = -1;
    private int bestCost = 0;
    private SystemComponent rootSystem;
    private BigInteger bestMask;
}
