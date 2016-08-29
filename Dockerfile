###
# vert.x docker POC using a Java REST Verticle
# To build:
#  docker build -t poc/vertx-rest-java .
# To run:
#  docker run -t -i -p 8080:8080 poc/vertx-rest-java
###

# Extend vert.x image
FROM vertx/vertx3

ENV VERTICLE_NAME be.rombit.romcore.docker.vertx.poc.ProductRestVerticle
ENV VERTICLE_FILE target/poc-product-service-0.0.1.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your verticle to the container
COPY $VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
