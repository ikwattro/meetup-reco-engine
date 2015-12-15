package com.graphaware.meetup.engine;

import com.graphaware.meetup.domain.RelationshipTypes;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.engine.SingleScoreRecommendationEngine;
import com.graphaware.reco.generic.result.PartialScore;
import org.apache.commons.collections4.map.HashedMap;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Map;

public class TopicEngine extends SingleScoreRecommendationEngine<Node, Node>{

    @Override
    protected Map<Node, PartialScore> doRecommendSingle(Node node, Context<Node, Node> context) {
        Map<Node, PartialScore> result = new HashedMap<>();

        for (Relationship r : node.getRelationships(RelationshipTypes.MEMBER_OF, Direction.OUTGOING)) {
            Node group = r.getEndNode();
            for (Relationship r2 : group.getRelationships(RelationshipTypes.TAGS_GROUP, Direction.INCOMING)) {
                Node topic = r2.getStartNode();
                for (Relationship r3 : topic.getRelationships(RelationshipTypes.TAGS_GROUP, Direction.OUTGOING)) {
                    if (r3.getEndNode().getId() != group.getId()) {
                        addToResult(result, r3.getEndNode(), getTopicScore());
                    }
                }
            }
        }

        return result;
    }

    private PartialScore getTopicScore() {
        Map<String, Object> reasons = new HashedMap<>();
        reasons.put("same topic", 1);

        return new PartialScore(1, reasons);
    }

    @Override
    public String name() {
        return "TopicEngine";
    }
}
