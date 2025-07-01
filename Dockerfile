# Etapa de build
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa final
FROM amazoncorretto:17

# Argumentos de build, com valores default
ARG PROFILE=prod
ARG APP_VERSION=1.0.0.0

WORKDIR /app

# Copia o artefato gerado
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8081

# Define variáveis de ambiente dentro do container (default vazias para DB)
# Se você quiser por um valor default, pode colocar
ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""
ENV YUGITILIDADES_JWT_SECRET=""
ENV YGO_PRODECK_API_CARDINFO=""
ENV YGO_PRODECK_API_IMAGES_CARDS=""
ENV YGO_PRODECK_API_IMAGES_CARDS_SMALL=""
ENV YGO_PRODECK_API_DB_VERSION=""
ENV YGO_PRODECK_API_KEY=""
ENV CLOUDFLARE_R2_TOKEN_VALUE=""
ENV CLOUDFLARE_R2_ACCESS_KEY_ID=""
ENV CLOUDFLARE_R2_BUCKET_NAME=""
ENV CLOUDFLARE_R2_ENDPOINT=""
ENV CLOUDFLARE_R2_SECRET_ACCESS_KEY=""
ENV CLOUDFLARE_R2_REGION=""

# Aproveita os ARGs para setar ENV que, por padrão, estará no container
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

# Passo final: define como o container inicia
CMD ["java", "-jar", \
  "-Dspring.profiles.active=${ACTIVE_PROFILE}", \
  "-Dspring.datasource.url=${DB_URL}", \
  "-Dspring.datasource.username=${DB_USERNAME}", \
  "-Dspring.datasource.password=${DB_PASSWORD}", \
  "-Dyugitilidades.jwt.secret=${YUGITILIDADES_JWT_SECRET}", \
  "-Dygo-prodeck.api.cardinfo=${YGO_PRODECK_API_CARDINFO}", \
  "-Dygo-prodeck.api.images.cards=${YGO_PRODECK_API_IMAGES_CARDS}", \
  "-Dygo-prodeck.api.images.cards.small=${YGO_PRODECK_API_IMAGES_CARDS_SMALL}", \
  "-Dygo-prodeck.api.db.version=${YGO_PRODECK_API_DB_VERSION}", \
  "-Dygo-prodeck.api.key=${YGO_PRODECK_API_KEY}", \
  "-Dcloudflare.r2.token.value=${CLOUDFLARE_R2_TOKEN_VALUE}", \
  "-Dcloudflare.r2.access.key.id=${CLOUDFLARE_R2_ACCESS_KEY_ID}", \
  "-Dcloudflare.r2.bucket.name=${CLOUDFLARE_R2_BUCKET_NAME}", \
  "-Dcloudflare.r2.endpoint=${CLOUDFLARE_R2_ENDPOINT}", \
  "-Dcloudflare.r2.secret.access.key=${CLOUDFLARE_R2_SECRET_ACCESS_KEY}", \
  "-Dcloudflare.r2.region=${CLOUDFLARE_R2_REGION}", \
  "app.jar"]