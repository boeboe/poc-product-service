package be.rombit.romcore.poc.productservice;

import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlLoaders;
import guru.nidi.ramltester.jaxrs.CheckingWebTarget;
import guru.nidi.ramltester.junit.RamlMatchers;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by boeboe on 30/08/16.
 */
@RunWith(VertxUnitRunner.class)
public class RestVerticleTest {

    private static final String RAML_DEFINITION = "/webroot/api/product.raml";
    private static final String NEW_PRODUCT_EXAMPLE = "/webroot/api/product-new.sample.json";
    private static final String BASE_URI = "http://localhost:8080/";

    private static final RamlDefinition api = RamlLoaders.fromClasspath()
            .load(RAML_DEFINITION)
            .assumingBaseUri(BASE_URI);

    private ResteasyClient client = new ResteasyClientBuilder().build();
    private CheckingWebTarget checking;

    private Vertx vertx;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(RestVerticle.class.getName(), context.asyncAssertSuccess());
        checking = api.createWebTarget(client.target(BASE_URI));
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testGetAllProductsEndpoint(TestContext context) {
        checking.path("/products").request().get();
        Assert.assertThat(checking.getLastReport(), RamlMatchers.hasNoViolations());
    }

    @Test
    public void testAddProductEndpoint(TestContext context) throws URISyntaxException, IOException {
        String product = new String(Files.readAllBytes(
                Paths.get(getClass().getResource(NEW_PRODUCT_EXAMPLE).toURI())));
        checking.path("/products").request().post(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertThat(checking.getLastReport(), RamlMatchers.hasNoViolations());
    }
}
