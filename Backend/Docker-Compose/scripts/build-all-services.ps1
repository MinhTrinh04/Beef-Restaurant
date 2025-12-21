# Script build tat ca services su dung Maven Jib plugin

Write-Host "Bat dau build tat ca microservices..." -ForegroundColor Cyan

# Danh sach cac services can build
$services = @(
    "DiscoveryService",
    "GatewayService", 
    "UserService",
    "MenuService",
    "BasketService",
    "OrderingService",
    "PaymentService"
)

# Luu thu muc hien tai
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$backendDir = Split-Path -Parent (Split-Path -Parent $scriptDir)

# Build BuildingBlocks truoc (dependency chung)
Write-Host "Building BuildingBlocks..." -ForegroundColor Yellow
Set-Location "$backendDir\BuildingBlocks"
mvn clean install -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "BuildingBlocks build failed!" -ForegroundColor Red
    exit 1
}

# Build tung service
foreach ($service in $services) {
    Write-Host "Building $service..." -ForegroundColor Yellow
    Set-Location "$backendDir\$service"
    
    # Build Docker image bang Jib
    mvn compile jib:dockerBuild -DskipTests
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "$service build thanh cong!" -ForegroundColor Green
    } else {
        Write-Host "$service build that bai!" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "Tat ca services da build xong!" -ForegroundColor Green
Write-Host "Danh sach images:" -ForegroundColor Cyan
docker images | Select-String "minh28012004"
