# POC REST Micro-service running in a Docker Container

 
Vert.x-Web is a great fit for HTTP/REST MicroServices.

Here's a simple micro-service example which implements an API for a product catalogue.

The API allows you to list all products, retrieve details for a particular product and to add a new product.

Product information is provided in JSON.

**List all products:** 

    GET /products

**Get a product:** 

    GET /products/<product_id>

**Add a product:** 

    PUT /products/<product_id>


Run the server either in your IDE or on the command line, then open your browser and hit
[list products](http://localhost:8080/products) to start playing with the API.
