package org.example.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.model.Node;
import org.example.model.ParallelBlock;
import org.example.model.SeriesBlock;

import java.math.BigInteger;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Node.class, name = "node"),
        @JsonSubTypes.Type(value = SeriesBlock.class, name = "sequential"),
        @JsonSubTypes.Type(value = ParallelBlock.class, name = "parallel")
})
public interface SystemComponent {
    double evaluateLifetime(BigInteger mask);

    int calculateCost(BigInteger mask);

    void extractNodes(List<Node> allNodes);

    SystemComponent deepCopy();
}