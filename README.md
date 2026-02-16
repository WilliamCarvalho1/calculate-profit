# Calculate Profit – Full Stack Application

This project is a complete **Calculate Profit** system composed of:

- A **Spring Boot 3 / Java 17 backend** exposing a REST API for managing shipments and "cargos".
- An **Angular 17 frontend** that allows the user to search shipments, input income/costs, calculate profit or loss, and visualize the result.

It is structured as a multi-project repository:

- `calculate-profit-backend/`
- `calculate-profit-frontend/`

---

### Observation:
- As the Business Requirement are limited (in real life they would be more detailed and include edge cases, validation rules, etc.), I decided to include some more features that made sense to me in the context of a "Calculate Profit" system, such as:
  - The concept of "Shipment" as a logical grouping of "Cargos".
  - The ability to add multiple cargos to a shipment and see the aggregated profit/loss.
  - A simple UI with a search for shipments and a form to calculate profit for new cargos.
  - The possibility to delete cargos from a shipment
  - A landing page and a navigation sidebar for better UX.

## 1. Features

### Domain

- **Shipment**: logical grouping of cargos.
- **Cargo**: has `income`, `cost`, `additionalCost`, `totalCost`, and `profit`.

### Backend capabilities

All backend endpoints are under `/api/v1/shipments`:

- `POST /api/v1/shipments` – Create a **new shipment** and its **first cargo**.
- `GET /api/v1/shipments/{id}` – Get a shipment with its associated cargos.
- `DELETE /api/v1/shipments/{id}` – Delete a shipment.
- `POST /api/v1/shipments/{shipmentId}/cargos` – Add a new cargo to an existing shipment.
- `PATCH /api/v1/shipments/cargos/{cargoId}` – Update an existing cargo.
- `DELETE /api/v1/shipments/cargos/{cargoId}` – Delete a cargo.

All write operations validate the request and compute **total cost** and **profit** in the domain.

### Frontend capabilities

- **Logo and navigation sidebar** using Bootstrap layout and Angular Material for form controls & buttons.
- A **Calculate Profit** route with:
  - **Shipment Id search**: calls `GET /api/v1/shipments/{id}` and renders a zebra-style table with existing cargos.
  - **Income & Outcome card**: three input fields (Income, Cost, Additional Cost) and a **Calculate** button.
    - If a shipment is **selected**, Calculate will **add a cargo** to that shipment.
    - If **no shipment is selected**, Calculate will **create a new shipment and a cargo** in one step via `POST /api/v1/shipments`.
  - **Zebra table** listing all cargos for the current shipment:
    - Columns: Income, Total Cost, Profit or Loss.
    - A small red **delete icon** per row to delete the cargo using `DELETE /api/v1/shipments/cargos/{cargoId}`.
- **Welcome page** routed as the default landing page; the Calculate Profit view is accessed via the menu.
- **Error handling**:
  - If a backend or network error occurs, it is logged to the browser console.
  - A red alert is shown in the UI with a message tuned by status code (network, 4xx, 5xx).

---

## 2. Technologies

### Backend (calculate-profit-backend)

- Java 17
- Spring Boot 3.1 (Web, Validation)
- Spring Security with JWT
- Spring Data JPA
- H2 (in-memory)
- Flyway (database migrations)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, AssertJ
- Maven
- Hexagonal / Ports & Adapters architecture

See `calculate-profit-backend/README.md` for more backend-specific architectural details.

### Frontend (calculate-profit-frontend)

- Angular 17.1.0 (standalone components, new control flow `@if`, `@for`)
- Angular Material 17.3.1 (buttons, form fields, input, icons)
- Bootstrap 5.3.3 (layout and basic styling)
- RxJS 7.8
- Jasmine + Karma (unit tests + coverage)

---

## 3. Project Structure

```text
calculate-profit/
├── calculate-profit-backend/
│   ├── pom.xml
│   ├── src/main/java/com/studies/calculateprofit/...
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   ├── img/
│   │   └── migration/ (Flyway migrations)
│   └── src/test/java/com/studies/calculateprofit/...
└── calculate-profit-frontend/
    ├── angular.json
    ├── package.json
    ├── proxy.conf.json
    ├── src/
    │   ├── main.ts
    │   ├── app/
    │   │   ├── app.component.* (shell + sidebar + routing)
    │   │   ├── app.routes.ts
    │   │   ├── calculate-profit/
    │   │   │   ├── calculate-profit.component.ts/html/scss
    │   │   │   └── calculate-profit.service.ts
    │   │   └── welcome/
    │   │       └── welcome.component.*
    │   └── assets/logo.png
    └── src/test/
        ├── app.component.spec.ts
        ├── calculate-profit.component.spec.ts
        └── calculate-profit.service.spec.ts
```

---

## 4. Running the Backend

From the repo root:

```bash
cd calculate-profit-backend
mvn clean package
mvn spring-boot:run
```

By default the backend listens on **`http://localhost:8080`**.

To run the packaged JAR instead:

```bash
java -jar target/shipment-catalog-0.0.1-SNAPSHOT.jar
```

### API Documentation (Swagger)

Once the backend is running, open:

- `http://localhost:8080/swagger-ui.html`

You can explore and try out the endpoints (including shipment and cargo operations) from Swagger UI.

> Note: Depending on your configuration, you might need a JWT token for authenticated endpoints. See the backend README under `calculate-profit-backend/README.md` for detailed instructions.

---

## 5. Running the Frontend

From the repo root:

```bash
cd calculate-profit-frontend
npm install
npm start
```

This runs the Angular dev server at:

- `http://localhost:4200`

### Backend proxy

The frontend is configured (via `proxy.conf.json`) to forward API calls from `http://localhost:4200` to the backend at `http://localhost:8080`. Typical API URLs used in the frontend service are:

- `GET /api/v1/shipments/{id}`
- `POST /api/v1/shipments`
- `POST /api/v1/shipments/{shipmentId}/cargos`
- `DELETE /api/v1/shipments/cargos/{cargoId}`

The Angular dev server will proxy these to `http://localhost:8080` so you don’t need to deal with CORS during development.

---

## 6. Frontend Architecture

### Key frontend pieces

- **Model**
  - Frontend data models represent:
    - User input for calculation (income, cost, additional cost).
    - Shipment with its list of cargos.

- **Components**
  - `AppComponent`: overall layout, sidebar with logo and "Calculate Profit" menu entry, router outlet.
  - `WelcomeComponent`: simple landing page.
  - `CalculateProfitComponent`: main feature page for searching shipments, calculating profit, rendering table, and deleting cargos.

- **Service** (`CalculateProfitService`)
  - Wraps all calls to the backend:
    - `getShipment`, `getShipmentWithCargos`
    - `createShipment`
    - `createCargo`
    - `deleteCargo`
  - Exposes simple, typed methods for the component and handles the API URL base.

- **Forms**
  - `searchForm`: handles Shipment Id search.
  - `calculationForm`: handles income, cost, and additional cost; only the required fields are validated as needed.

- **Routing**
  - Configured via standalone `app.routes.ts` and `provideRouter(routes)` in `app.config.ts`.
  - Default route: `''` → `WelcomeComponent`.
  - Feature route: `'/calculate-profit'` → `CalculateProfitComponent`.

- **Error Handling**
  - Every HTTP call in the component uses `.subscribe({ next, error })`.
  - On error, `buildErrorMessage(context, error)`:
    - Distinguishes network failures (`status === 0`), client errors (4xx), and server errors (5xx).
    - Uses backend `message`/`detail` field if present.
    - Sets a user-facing `errorMessage` property.
  - The template shows a red Bootstrap alert whenever `errorMessage` is non-null.

---

## 7. Tests & Coverage

### Backend tests

From `calculate-profit-backend/`:

```bash
mvn test
```

You can also run with coverage plugins such as JaCoCo if configured (not shown here by default).

### Frontend tests

From `calculate-profit-frontend/`:

```bash
npm test
```

To run once with coverage (as used during development):

```bash
ng test --code-coverage --watch=false
```

This will generate a coverage report under `calculate-profit-frontend/coverage/`.

The frontend includes unit tests for:

- `AppComponent` (shell + menu + routing presence)
- `CalculateProfitComponent` (interaction with service, form behavior)
- `CalculateProfitService` (correct calls to API endpoints via `HttpTestingController`)

---

## 8. Error Handling & Logging

Across the system, errors are both **logged** and **surfaced to the user**:

- **Backend**
  - Uses standard Spring Boot exception handling plus a global exception handler (`@ControllerAdvice`) to log errors and map them to HTTP responses.
  - Validation errors produce well-structured 4xx responses with messages.

- **Frontend**
  - All HTTP errors in the Calculate Profit feature are logged to the browser console with contextual messages.
  - Users see a descriptive error banner in the UI so they know the operation failed.
