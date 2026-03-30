# ─────────────────────────────────────────────────────────────────────────────
# Dockerfile — Swag Labs Selenide Tests
#
# Multi-stage build:
#   Stage 1 (deps)  : download Maven dependencies to layer-cache them
#   Stage 2 (runner): install Chrome, copy source, run tests
#
# Usage:
#   docker build -t swag-tests .
#   docker run --rm swag-tests                              # all tests
#   docker run --rm -e SUITE=smoke swag-tests               # smoke only
#   docker run --rm -e SUITE=regression swag-tests          # regression only
# ─────────────────────────────────────────────────────────────────────────────

# ── Stage 1: dependency cache ────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS deps
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# ── Stage 2: test runner ─────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS runner

# Install Google Chrome stable
RUN apt-get update -qq && \
    apt-get install -y -qq \
        wget gnupg2 ca-certificates apt-transport-https \
        fonts-liberation libappindicator3-1 libasound2 libatk-bridge2.0-0 \
        libatk1.0-0 libcairo2 libcups2 libdbus-1-3 libexpat1 \
        libfontconfig1 libgbm1 libgcc1 libglib2.0-0 libgtk-3-0 \
        libnspr4 libnss3 libpango-1.0-0 libpangocairo-1.0-0 \
        libstdc++6 libx11-6 libx11-xcb1 libxcb1 libxcomposite1 \
        libxcursor1 libxdamage1 libxext6 libxfixes3 libxi6 libxrandr2 \
        libxrender1 libxss1 libxtst6 lsb-release xdg-utils unzip && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub \
        | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] \
        http://dl.google.com/linux/chrome/deb/ stable main" \
        > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update -qq && \
    apt-get install -y -qq google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

# Copy cached Maven repo from deps stage
COPY --from=deps /root/.m2 /root/.m2

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Environment — headless Chrome, no sandbox (required in container)
ENV HEADLESS=true
ENV BROWSER=chrome
# Pass extra Chrome args via system property
ENV JAVA_OPTS="-Dwebdriver.chrome.args='--no-sandbox --disable-dev-shm-usage --disable-gpu'"

# SUITE env var: leave blank for all, set to 'smoke' or 'regression' to filter
ENV SUITE=""

ENTRYPOINT ["sh", "-c", \
  "if [ -n \"$SUITE\" ]; then \
     mvn test -P$SUITE -Dheadless=$HEADLESS -Dbrowser=$BROWSER --no-transfer-progress; \
   else \
     mvn test -Dheadless=$HEADLESS -Dbrowser=$BROWSER --no-transfer-progress; \
   fi"]