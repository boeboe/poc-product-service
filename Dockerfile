###
# vert.x docker POC using a Java REST Verticle
# To build:
#  docker build -t poc-product-service .
# To run:
#  docker run -t -i -p 8080:8080 poc-product-service
###

# Extend Oracle Java8 image
FROM ventura24/java-8-alpine

ENV VERTICLE_NAME be.rombit.romcore.docker.vertx.poc.ProductRestVerticle
ENV VERTICLE_FILE poc-product-service-1.0.0.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your verticle to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $VERTICLE_FILE"]
