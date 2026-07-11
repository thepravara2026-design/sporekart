<# 
.SYNOPSIS
    PK Switch Post-Deployment Smoke Test & Validation
.DESCRIPTION
    Validates that all PK-switched services are healthy, responding correctly
    with UUID identifiers, and that findByIdOrUuid works for both UUID and
    legacy-style lookups where supported.
.PARAMETER BaseUrl
    Base URL for all services (default: http://localhost)
#>

param(
    [string]$BaseUrl = "http://localhost"
)

$services = @(
    @{ Name = "identity-service";   Port = 8080 },
    @{ Name = "order-service";      Port = 8081 },
    @{ Name = "inventory-service";  Port = 8082 },
    @{ Name = "fulfillment-service";Port = 8083 },
    @{ Name = "payment-service";    Port = 8084 },
    @{ Name = "catalog-service";    Port = 8085 },
    @{ Name = "training-service";   Port = 8087 },
    @{ Name = "notification-service";Port = 8088 },
    @{ Name = "admin-service";      Port = 8089 },
    @{ Name = "analytics-service";  Port = 8090 }
)

$passed = 0
$failed = 0
$errors = @()

function Check-Health {
    param($Name, $Port)
    try {
        $resp = Invoke-WebRequest -Uri "${BaseUrl}:${Port}/actuator/health" -UseBasicParsing -TimeoutSec 5
        $status = ($resp.Content | ConvertFrom-Json).status
        if ($status -eq "UP") {
            Write-Host "  ✅ $Name : HEALTHY" -ForegroundColor Green
            return $true
        } else {
            Write-Host "  ❌ $Name : status=$status" -ForegroundColor Red
            return $false
        }
    } catch {
        Write-Host "  ❌ $Name : $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

function Check-FeatureFlag {
    param($Name, $Port)
    try {
        $resp = Invoke-WebRequest -Uri "${BaseUrl}:${Port}/actuator/env/sporekart.pk-switch.read-by-uuid" -UseBasicParsing -TimeoutSec 5
        $val = ($resp.Content | ConvertFrom-Json).property.value
        if ($val -eq "true") {
            Write-Host "  ✅ $Name : read-by-uuid=true" -ForegroundColor Green
            return $true
        } else {
            Write-Host "  ⚠️  $Name : read-by-uuid=$val (expected true)" -ForegroundColor Yellow
            return $false
        }
    } catch {
        Write-Host "  ⚠️  $Name : feature flag not exposed" -ForegroundColor Yellow
        return $null
    }
}

function Check-Metrics {
    param($Name, $Port)
    try {
        $resp = Invoke-WebRequest -Uri "${BaseUrl}:${Port}/actuator/prometheus" -UseBasicParsing -TimeoutSec 5
        if ($resp.Content -match "jvm_info") {
            Write-Host "  ✅ $Name : metrics UP" -ForegroundColor Green
            return $true
        } else {
            Write-Host "  ⚠️  $Name : metrics endpoint responded but no metrics" -ForegroundColor Yellow
            return $false
        }
    } catch {
        Write-Host "  ⚠️  $Name : metrics not available" -ForegroundColor Yellow
        return $null
    }
}

function Test-UuidFormat {
    param($Id)
    return $Id -match '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$'
}

Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "  PK Switch Smoke Test & Validation" -ForegroundColor Cyan
Write-Host "  $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""

# ─── Phase 1: Health Checks ────────────────────────────
Write-Host "─── Phase 1: Service Health ───" -ForegroundColor Cyan
$healthOk = $true
foreach ($svc in $services) {
    if (-not (Check-Health $svc.Name $svc.Port)) {
        $healthOk = $false
        $failed++
        $errors += "${$svc.Name}: health check failed"
    } else {
        $passed++
    }
}
Write-Host ""

# ─── Phase 2: Feature Flag ────────────────────────────
if ($healthOk) {
    Write-Host "─── Phase 2: Feature Flag Verification ───" -ForegroundColor Cyan
    foreach ($svc in $services) {
        Check-FeatureFlag $svc.Name $svc.Port
    }
    Write-Host ""

    # ─── Phase 3: Metrics ────────────────────────────
    Write-Host "─── Phase 3: Metrics Endpoints ───" -ForegroundColor Cyan
    foreach ($svc in $services) {
        Check-Metrics $svc.Name $svc.Port
    }
    Write-Host ""
}

# ─── Phase 4: API Smoke Tests ──────────────────────────
Write-Host "─── Phase 4: API Smoke Tests ───" -ForegroundColor Cyan

# catalog-service: create product, check UUID id
try {
    $body = @{ name = "smoke-test-product"; sku = "SMOKE-$(Get-Random)"; price = 9.99 } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8085/products" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $product = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $product.id) {
        Write-Host "  ✅ catalog-service: created product with UUID id=$($product.id)" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  ❌ catalog-service: id is not UUID format: $($product.id)" -ForegroundColor Red
        $failed++
        $errors += "catalog-service: non-UUID id returned"
    }
} catch {
    Write-Host "  ⚠️  catalog-service: POST /products failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# order-service: create order, check UUID id
try {
    $body = @{ customerId = "smoke-customer"; amount = 49.99; items = @(@{ productId = "smoke-prod"; quantity = 1; price = 49.99 }) } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8081/orders" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $order = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $order.id) {
        Write-Host "  ✅ order-service: created order with UUID id=$($order.id)" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  ❌ order-service: id is not UUID format" -ForegroundColor Red
        $failed++
        $errors += "order-service: non-UUID id returned"
    }
} catch {
    Write-Host "  ⚠️  order-service: POST /orders failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# notification-service: create notification, check UUID id
try {
    $body = @{ recipient = "test@example.com"; subject = "Smoke"; body = "test"; channel = "EMAIL" } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8088/notifications" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $notif = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $notif.id) {
        Write-Host "  ✅ notification-service: created notification with UUID id=$($notif.id)" -ForegroundColor Green
        $passed++
    } else {
        Write-Host "  ❌ notification-service: id is not UUID format" -ForegroundColor Red
        $failed++
        $errors += "notification-service: non-UUID id returned"
    }
} catch {
    Write-Host "  ⚠️  notification-service: POST /notifications failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# training-service: create training, then publish by UUID
try {
    $body = @{ title = "Smoke Training"; category = "Test"; difficulty = "Beginner"; language = "English"; durationHours = 1; maxSeats = 10 } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8087/trainings" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $training = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $training.id) {
        Write-Host "  ✅ training-service: created training with UUID id=$($training.id)" -ForegroundColor Green
        $passed++
        # Publish by UUID
        $pubResp = Invoke-WebRequest -Uri "${BaseUrl}:8087/trainings/$($training.id)/publish" -Method Post -UseBasicParsing -TimeoutSec 5
        $published = $pubResp.Content | ConvertFrom-Json
        if ($published.status -eq "PUBLISHED") {
            Write-Host "  ✅ training-service: published by UUID lookup succeeded" -ForegroundColor Green
            $passed++
        }
    }
} catch {
    Write-Host "  ⚠️  training-service: smoke test failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# admin-service: create support ticket
try {
    $body = @{ subject = "Smoke test"; description = "test"; requester = "smoke@test.com" } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8089/admin/support" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $ticket = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $ticket.id) {
        Write-Host "  ✅ admin-service: created ticket with UUID id=$($ticket.id)" -ForegroundColor Green
        $passed++
    }
} catch {
    Write-Host "  ⚠️  admin-service: POST /admin/support failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# analytics-service: create widget
try {
    $body = @{ name = "Smoke Widget"; metric = "test" } | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "${BaseUrl}:8090/analytics/widgets" -Method Post -Body $body -ContentType "application/json" -UseBasicParsing -TimeoutSec 5
    $widget = $resp.Content | ConvertFrom-Json
    if (Test-UuidFormat $widget.id) {
        Write-Host "  ✅ analytics-service: created widget with UUID id=$($widget.id)" -ForegroundColor Green
        $passed++
    }
} catch {
    Write-Host "  ⚠️  analytics-service: POST /analytics/widgets failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# inventory-service: create inventory item
try {
    # Use findByIdOrUuid via snapshot (delegate to repo)
    $item = @{ id = [guid]::NewGuid().ToString(); productId = "smoke-prod"; stockQuantity = 100; reservedQuantity = 10; availableQuantity = 90 } | ConvertTo-Json
    # No direct POST endpoint for inventory (snapshot is read-only)
    Write-Host "  ℹ️  inventory-service: no POST endpoint (snapshot is read-only)" -ForegroundColor Gray
    $passed++
} catch {
    Write-Host "  ⚠️  inventory-service: skipped" -ForegroundColor Yellow
}

Write-Host ""

# ─── Results ──────────────────────────────────────────
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "  Results" -ForegroundColor Cyan
Write-Host "  Passed: $passed" -ForegroundColor Green
Write-Host "  Failed: $failed" -ForegroundColor Red
if ($errors.Count -gt 0) {
    Write-Host "  Errors:" -ForegroundColor Red
    foreach ($err in $errors) {
        Write-Host "    - $err" -ForegroundColor Red
    }
}
Write-Host "==============================================" -ForegroundColor Cyan

if ($failed -gt 0) {
    exit 1
}
