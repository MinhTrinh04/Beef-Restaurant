# Script export Keycloak realm configuration

$REALM_NAME = "master"
$KEYCLOAK_URL = "http://localhost:8180"
$ADMIN_USER = "admin"
$ADMIN_PASS = "admin"
$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$OUTPUT_DIR = Join-Path (Split-Path -Parent $SCRIPT_DIR) "keycloak"
$OUTPUT_FILE = Join-Path $OUTPUT_DIR "realm-export.json"

Write-Host "Bat dau export Keycloak realm: $REALM_NAME" -ForegroundColor Cyan

# Kiem tra Keycloak co dang chay khong
try {
    $response = Invoke-WebRequest -Uri $KEYCLOAK_URL -TimeoutSec 5 -UseBasicParsing
    Write-Host "Keycloak dang chay" -ForegroundColor Green
} catch {
    Write-Host "Keycloak khong chay tai $KEYCLOAK_URL" -ForegroundColor Red
    Write-Host "Vui long chay: docker-compose up -d keycloak" -ForegroundColor Yellow
    exit 1
}

# Tao thu muc output neu chua co
if (-not (Test-Path $OUTPUT_DIR)) {
    New-Item -ItemType Directory -Path $OUTPUT_DIR | Out-Null
}

# Export realm bang Keycloak Admin CLI trong container
Write-Host "Dang export realm..." -ForegroundColor Yellow
docker exec keycloak /opt/keycloak/bin/kc.sh export --dir /tmp --realm $REALM_NAME --users realm_file

if ($LASTEXITCODE -ne 0) {
    Write-Host "Export that bai!" -ForegroundColor Red
    exit 1
}

# Copy file export ra ngoai
docker cp "keycloak:/tmp/$REALM_NAME-realm.json" $OUTPUT_FILE

if ($LASTEXITCODE -eq 0) {
    Write-Host "Export thanh cong!" -ForegroundColor Green
    Write-Host "File: $OUTPUT_FILE" -ForegroundColor Cyan
    Write-Host ""
    
    # Doc va hien thi thong tin realm
    $realmContent = Get-Content $OUTPUT_FILE -Raw
    $clientCount = ([regex]::Matches($realmContent, "`"clientId`"")).Count
    $userCount = ([regex]::Matches($realmContent, "`"username`"")).Count
    
    Write-Host "Noi dung realm:" -ForegroundColor Cyan
    Write-Host "   - Realm: $REALM_NAME"
    Write-Host "   - Clients: $clientCount"
    Write-Host "   - Users: $userCount"
} else {
    Write-Host "Export that bai!" -ForegroundColor Red
    exit 1
}
