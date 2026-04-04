# Ecommerce API

## Docker

Build the application image:

```bash
docker build -t ecommerce-api .
```

Run the container with environment variables from `.env`:

```bash
docker run --env-file .env -p 8080:8080 -v "${PWD}/uploads:/app/uploads" ecommerce-api
```

If you are using the existing `docker-compose.yml` for MySQL, Redis, and RabbitMQ, start those services first:

```bash
docker-compose up -d mysql redis rabbitmq
```

Then run the application container using the command above.
