MAILHOG
docker run -d --name mailhog -p 1025:1025 -p 8025:8025 --restart always mailhog/mailhog

DOCKER (SQL)
docker run --name candidate-assessment -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=candidate-assessment -p 5432:5432 -d postgres