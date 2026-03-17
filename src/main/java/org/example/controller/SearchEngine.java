package org.example.controller;

import org.example.components.SystemComponent;
import org.example.model.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface SearchEngine {
    void doSearch(List<Node> allNodes, SystemComponent rootSystem, int budget);

    int getBestCost();
    int getBestLifetime();
    Set<Integer> getBestCombination();

    String getName();
}
