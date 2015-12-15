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

import java.util.HashMap;
import java.util.Map;

public class MemberEngine extends SingleScoreRecommendationEngine<Node, Node> {

    @Override
    protected Map<Node, PartialScore> doRecommendSingle(Node node, Context<Node, Node> context) {
        GraphDatabaseService database = node.getGraphDatabase();
        Map<Node, PartialScore> result = new HashedMap<>();

        for (Relationship r : node.getRelationships(RelationshipTypes.MEMBER_OF, Direction.OUTGOING)) {
            Node group = r.getEndNode();
            for (Relationship r2 : group.getRelationships(RelationshipTypes.MEMBER_OF, Direction.INCOMING)) {
                Node otherMember = r2.getStartNode();
                for (Relationship r3 : otherMember.getRelationships(RelationshipTypes.MEMBER_OF, Direction.OUTGOING)) {
                    addToResult(result, r3.getEndNode(), getScore());
                }
            }
        }

        return result;
    }

    private PartialScore getScore() {
        HashMap<String, Object> reasons = new HashMap<>();
        reasons.put("from member", 1);

        return new PartialScore(1, reasons);
    }

    @Override
    public String name() {
        return "memberEngine";
    }
}
