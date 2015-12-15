package com.graphaware.meetup.processor;

import com.graphaware.meetup.domain.RelationshipTypes;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.post.BasePostProcessor;
import com.graphaware.reco.generic.result.Recommendation;
import com.graphaware.reco.generic.result.Recommendations;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class PenalizeBigGroups extends BasePostProcessor<Node, Node> {

    @Override
    protected String name() {
        return "penalizeBigGroups";
    }

    @Override
    protected void doPostProcess(Recommendations<Node> recommendations, Node node, Context<Node, Node> context) {
        for (Recommendation<Node> recommendation : recommendations.get()) {
            if (recommendation.getItem().getDegree(RelationshipTypes.MEMBER_OF, Direction.INCOMING) > 15) {
                recommendation.add(name(), -5);
            }
        }
    }
}
