package com.graphaware.meetup;

import com.graphaware.meetup.domain.Labels;
import com.graphaware.reco.generic.config.SimpleConfig;
import com.graphaware.reco.generic.engine.TopLevelRecommendationEngine;
import com.graphaware.reco.generic.log.Slf4jRecommendationLogger;
import com.graphaware.reco.generic.result.Recommendation;
import com.graphaware.test.integration.DatabaseIntegrationTest;
import org.junit.After;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.graphaware.common.util.IterableUtils.getSingle;
import static org.junit.Assert.assertFalse;

public class MyRecommendationEngineIntegrationTest extends DatabaseIntegrationTest {

    private TopLevelRecommendationEngine<Node, Node> engine;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        engine = new MyRecommendationEngine();
    }

    @After
    public void shutdown() {
        getDatabase().shutdown();
    }

    @Override
    protected GraphDatabaseService createDatabase(){
        File pathToDb = new File(getClass().getClassLoader().getResource("test.db").getFile());

        GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(pathToDb)
                .newGraphDatabase();

        return graphDatabaseService;
    }

    @Test
    public void shouldRecommendSomething() {
        try (Transaction tx = getDatabase().beginTx()) {

            Node member = getDatabase().findNode(Labels.Member, "name", "Nicolas Mervaillie");
            List<Recommendation<Node>> recoForNico = engine.recommend(member, new SimpleConfig(2));
            assertFalse(recoForNico.isEmpty());

            printRecommendations(member, recoForNico);
        }
    }

    private void printRecommendations(Node input, List<Recommendation<Node>> recommendations) {
        System.out.println(new Slf4jRecommendationLogger<Node, Node>().toString(input, recommendations, null));
    }
}
