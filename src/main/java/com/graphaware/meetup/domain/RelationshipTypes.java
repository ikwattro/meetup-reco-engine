package com.graphaware.meetup.domain;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType{
    MEMBER_OF,
    TAGS_GROUP,
    GROUP_IN_CITY,
    LIVES_IN
}
