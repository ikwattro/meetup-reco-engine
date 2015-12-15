package com.graphaware.meetup.filter;

import com.graphaware.meetup.domain.RelationshipTypes;
import com.graphaware.reco.generic.config.Config;
import com.graphaware.reco.generic.filter.BlacklistBuilder;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashSet;
import java.util.Set;

public class GroupsBlackList implements BlacklistBuilder<Node, Node> {

    @Override
    public Set<Node> buildBlacklist(Node node, Config config) {
        Set<Node> blackList = new HashSet<>();

        for (Relationship r : node.getRelationships(RelationshipTypes.MEMBER_OF)) {
            blackList.add(r.getEndNode());
        }

        return blackList;
    }
}
