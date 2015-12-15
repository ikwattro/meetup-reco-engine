package com.graphaware.meetup.processor;

import com.graphaware.meetup.domain.RelationshipTypes;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.post.BasePostProcessor;
import com.graphaware.reco.generic.result.Recommendation;
import com.graphaware.reco.generic.result.Recommendations;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class RewardSameCity extends BasePostProcessor<Node, Node> {

    @Override
    protected String name() {
        return "sameCity";
    }

    @Override
    protected void doPostProcess(Recommendations<Node> recommendations, Node node, Context<Node, Node> context) {
        Node userCity = node.getSingleRelationship(RelationshipTypes.LIVES_IN, Direction.OUTGOING).getEndNode();

        for (Recommendation<Node> recommendation : recommendations.get()) {
            Node groupCity = recommendation.getItem().getSingleRelationship(RelationshipTypes.GROUP_IN_CITY, Direction.OUTGOING).getEndNode();
            if (groupCity.getId() == userCity.getId()) {
                recommendation.add(name(), 15);
            }
        }
    }
}
