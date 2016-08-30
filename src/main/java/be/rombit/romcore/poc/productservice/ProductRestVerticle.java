package be.rombit.romcore.poc.productservice;

import com.jcabi.manifests.Manifests;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductRestVerticle extends AbstractVerticle {

    private Map<String, JsonObject> products = new HashMap<>();

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(ProductRestVerticle.class);
    }

    @Override
    public void start() {

        setUpInitialData();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/").handler(this::handleServiceStatus);
        router.get("/products").handler(this::handleListProducts);
        router.get("/products/:productID").handler(this::handleGetProduct);
        router.post("/products").handler(this::handleAddProduct);
        router.put("/products/:productID").handler(this::handleUpdateProduct);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void handleServiceStatus(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String name = Manifests.read("Service-Name");
        String version = Manifests.read("Service-Version");

        JsonObject status = new JsonObject().put("name", name).put("version", version).put("status", "up");
        response.putHeader("content-type", "application/json").end(status.encodePrettily());
    }

    private void handleListProducts(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        products.forEach((k, v) -> arr.add(v));
        routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
    }

    private void handleGetProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = products.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleAddProduct(RoutingContext routingContext) {
        String productID = UUID.randomUUID().toString();
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = routingContext.getBodyAsJson();
            product.put("id", productID);
            if (product == null) {
                sendError(400, response);
            } else {
                products.put(productID, product);
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleUpdateProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = routingContext.getBodyAsJson();
            if (product == null) {
                sendError(400, response);
            } else {
                products.put(productID, product);
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData() {
        addProduct(new JsonObject().put("id", UUID.randomUUID().toString()).put("name", "Egg Whisk").put("price", 3.99)
                .put("weight", 150));
        addProduct(new JsonObject().put("id", UUID.randomUUID().toString()).put("name", "Tea Cosy").put("price", 5.99)
                .put("weight", 100));
        addProduct(new JsonObject().put("id", UUID.randomUUID().toString()).put("name", "Spatula").put("price", 1.00)
                .put("weight", 80));
    }

    private void addProduct(JsonObject product) {
        products.put(product.getString("id"), product);
    }
}
