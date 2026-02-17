# Feed Normalization Service

A Spring Boot microservice that receives sports feed data from multiple providers (Alpha and Beta), normalizes it into a unified internal format, and publishes the resulting messages. It acts as the standardization layer in a feed processing pipeline for 1X2 market data (odds updates and bet settlements).

## Table of contents

- [Prerequisites](#prerequisites)
- [How to run](#how-to-run)
- [API reference](#api-reference)
- [Message formats](#message-formats)
- [Responses and errors](#responses-and-errors)
- [Architecture](#architecture)
- [Project structure](#project-structure)
- [Configuration](#configuration)
- [Testing](#testing)
- [Building for production](#building-for-production)

---

## Prerequisites

- **Java 17** or later  
- **Apache Maven 3.6+** (or use the included Maven Wrapper)

Check your environment:

```bash
java -version
./mvnw -v
```

---

## How to run

### Using Maven Wrapper (recommended)

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### Using installed Maven

```bash
mvn spring-boot:run
```

### Run the packaged JAR

```bash
./mvnw clean package
java -jar target/feed-normalization-service-0.0.1-SNAPSHOT.jar
```

The application listens on **port 8080** by default.

---

## API reference

All endpoints expect **POST** requests with **Content-Type: application/json**. Each request must contain exactly one message (either an odds update or a bet settlement).

### Provider Alpha — `POST /provider-alpha/feed`

Receives feed data in Alpha’s format. See [Message formats](#message-formats) for the exact JSON shape.

### Provider Beta — `POST /provider-beta/feed`

Receives feed data in Beta’s format. See [Message formats](#message-formats) for the exact JSON shape.

---

## Message formats

### Provider Alpha

**ODDS_CHANGE** (odds update):

```json
{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": {
    "1": 2.0,
    "X": 3.1,
    "2": 3.8
  }
}
```

- `1` = home win, `X` = draw, `2` = away win.

**BET_SETTLEMENT** (settlement):

```json
{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1"
}
```

- `outcome`: one of `"1"`, `"X"`, or `"2"`.

### Provider Beta

**ODDS_CHANGE** (odds update):

```json
{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": {
    "home": 1.95,
    "draw": 3.2,
    "away": 4.0
  }
}
```

**BET_SETTLEMENT** (settlement):

```json
{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away"
}
```

- `result`: one of `"home"`, `"draw"`, or `"away"`.

### Example requests

**Alpha — odds update**

```bash
curl -X POST http://localhost:8080/provider-alpha/feed \
  -H "Content-Type: application/json" \
  -d '{"msg_type": "odds_update", "event_id": "ev123", "values": {"1": 2.0, "X": 3.1, "2": 3.8}}'
```

**Alpha — settlement**

```bash
curl -X POST http://localhost:8080/provider-alpha/feed \
  -H "Content-Type: application/json" \
  -d '{"msg_type": "settlement", "event_id": "ev123", "outcome": "1"}'
```

**Beta — odds update**

```bash
curl -X POST http://localhost:8080/provider-beta/feed \
  -H "Content-Type: application/json" \
  -d '{"type": "ODDS", "event_id": "ev456", "odds": {"home": 1.95, "draw": 3.2, "away": 4.0}}'
```

**Beta — settlement**

```bash
curl -X POST http://localhost:8080/provider-beta/feed \
  -H "Content-Type: application/json" \
  -d '{"type": "SETTLEMENT", "event_id": "ev456", "result": "away"}'
```

---

## Responses and errors

| Status | Meaning |
|--------|--------|
| **200 OK** | Payload accepted and processed; message normalized and published (mock publisher in this implementation). |
| **400 Bad Request** | Client error: invalid JSON, missing required fields (`msg_type`/`type`, `event_id`, `values`/`odds`, `outcome`/`result`), unsupported message type, or invalid outcome/result value. |
| **500 Internal Server Error** | Unexpected server error during processing. |

Responses do not include a body; only the status code indicates success or failure.

---

## Architecture

1. **Controllers** — Accept POST body and delegate to the processing service by provider.
2. **Feed processing service** — Resolves the provider’s adapter, transforms the payload, and publishes the normalized message.
3. **Adapters** — Parse provider-specific JSON and select the right transformation strategy (by provider + message type).
4. **Strategies** — Map raw JSON to a standard internal model (`StandardOddsChange` or `StandardBetSettlement`).
5. **Publisher** — Receives the standard message (currently a mock that logs; can be replaced with a real queue).

Normalized messages are provider-agnostic and include `eventId`, `messageType` (ODDS_CHANGE or BET_SETTLEMENT), `provider`, and either odds (1X2) or settlement result.

---

## Project structure

```
src/main/java/org/sporty/feed/
├── FeedNormalizationServiceApplication.java
├── controller/           # REST endpoints and global exception handler
├── service/              # Feed processing orchestration
├── adapter/              # Provider-specific ingestion
│   ├── alpha/            # Alpha feed adapter
│   └── beta/             # Beta feed adapter
├── strategy/             # Message-type transformation (Strategy pattern)
├── domain/               # Shared types, enums, standard message model
├── exception/            # Client-facing exceptions (e.g. invalid payload)
└── publisher/            # Output abstraction (mock implementation)
```

| Package      | Role |
|-------------|------|
| `controller/` | Alpha and Beta endpoints; `FeedExceptionHandler` maps exceptions to 400/500. |
| `service/`    | `FeedProcessingService`: get adapter → transform → publish. |
| `adapter/`    | `FeedAdapter` implementations parse JSON and delegate to strategies. |
| `strategy/`   | One strategy per (provider, message type); produce `StandardMessage`. |
| `domain/`     | `Provider`, `MessageType`, `MarketOutcome`, `StandardMessage`, `MessageTypeKey`. |
| `exception/`  | `InvalidPayloadException` for unparseable payloads (→ 400). |
| `publisher/`  | `MessagePublisher` interface; current implementation logs (mock). |

---

## Configuration

Main settings: `src/main/resources/application.properties`

| Property | Default | Description |
|----------|--------|-------------|
| `spring.application.name` | `feed-normalization-service` | Application name. |
| `server.port` | `8080` | Server port (add this property to change it). |

Example — use port 9090:

```properties
server.port=9090
```

---

## Testing

Run all tests:

```bash
./mvnw test
```

Coverage includes:

- **Controllers** — Valid and invalid payloads, missing fields, unsupported message types, invalid outcome/result; expectations for 200, 400, and 500.
- **Service** — Delegation to adapter and publisher; exception propagation.
- **Adapters** — Transformation for all supported message types and error cases.
- **Strategies** — Strategy selection via `MessageTypeKey` and transformation output.
- **Factory** — Correct adapter per provider.
- **Domain** — Getters, `Provider.from()`, immutability of standard messages.
- **Integration** — Full request flow with spy on the publisher.

Test-specific configuration (e.g. disabling WebDriver for MockMvc) is in `src/test/resources/application.properties` when required.

---

## Building for production

```bash
./mvnw clean package -DskipTests
```

Artifact: `target/feed-normalization-service-0.0.1-SNAPSHOT.jar`

Run it:

```bash
java -jar target/feed-normalization-service-0.0.1-SNAPSHOT.jar
```
