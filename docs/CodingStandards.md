# Coding Standards

## Java Version
- Java 21
- Use `record` for DTOs and immutable data carriers
- Use `var` only when type is obvious from right-hand side
- No `lombok` in business code (prefer records and manual constructors)

## Imports
- No wildcard imports (`import foo.*`)
- Group imports: `java.*`, `javax.*`/`jakarta.*`, third-party, project
- Remove unused imports before committing

## Null Safety
- Prefer `Optional` over nullable returns
- Use `java.util.Objects.requireNonNull()` for constructor validation
- Annotate nullable parameters with `@Nullable`
- Avoid returning `null` from methods — return `Optional.empty()` or empty collections

## Exception Handling
- Never catch and ignore exceptions
- Never throw raw `RuntimeException` or `Exception`
- Always use specific `SporekartException` subtypes
- Use `GlobalExceptionHandler` — don't write custom `@ExceptionHandler` in controllers

## Testing
- JUnit 5 + AssertJ
- Test class naming: `{ClassUnderTest}Test`
- Method naming: `test{Scenario}` or `{method}_{scenario}_expects_{result}`
- Aim for 95%+ coverage on new code
- Test edge cases: null inputs, empty collections, boundary values

## Logging
- Use SLF4J `LoggerFactory.getLogger()`
- Log at appropriate level: ERROR (failures), WARN (recoverable), INFO (state changes), DEBUG (details)
- Include correlationId via MDC (auto-populated by CorrelationIdFilter)
- Never log sensitive data (passwords, tokens, PII)

## Dependency Injection
- Constructor injection only
- No field injection (`@Autowired` on fields)
- One class per file
- Keep classes under 300 lines; split large classes

## REST API Conventions
- Use `@RestController` (not `@Controller`)
- Return `ResponseEntity<ApiResponse<T>>` or `ResponseEntity<ApiPageResponse<T>>`
- Use `@Valid` on request bodies
- Use `@PreAuthorize` for security
- Consistent URL patterns: `/api/v1/{resource}`
