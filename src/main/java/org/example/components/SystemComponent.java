package org.example.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.example.model.Node;
import org.example.model.ParallelBlock;
import org.example.model.SeriesBlock;

import java.util.List;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Node.class, name = "node"),
        @JsonSubTypes.Type(value = SeriesBlock.class, name = "series"),
        @JsonSubTypes.Type(value = ParallelBlock.class, name = "parallel")
})
public interface SystemComponent {
    int evaluateLifetime(Set<Integer> activeRedundancies);
    int calculateCost(Set<Integer> activeRedundancies);
    void extractNodes(List<Node> allNodes);
}