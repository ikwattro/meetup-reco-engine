package com.graphaware.meetup;

import com.graphaware.test.data.DatabasePopulator;
import com.graphaware.test.data.GraphgenPopulator;
import com.graphaware.test.integration.GraphAwareApiTest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class MyRecommendationEngineEnd2EndTest extends GraphAwareApiTest {

    @Override
    protected GraphDatabaseService createDatabase(){
        File pathToDb = new File(getClass().getClassLoader().getResource("test.db").getFile());

        GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(pathToDb.getAbsolutePath())
                .newGraphDatabase();

        return graphDatabaseService;
    }

    @Test
    public void shouldRecommendSomething() throws IOException {
        String response = httpClient.get(baseUrl() + "/recommendation/Nicolas%20Mervaillie", HttpStatus.OK_200);

        System.out.println("=== RESULT ===");
        System.out.println(response);
        System.out.println("=== END ===");
    }

    @Test
    public void shouldReturn404IfNodeNotFound() {
        httpClient.get(baseUrl() + "/recommendation/NonExisting", HttpStatus.NOT_FOUND_404);
    }
}
