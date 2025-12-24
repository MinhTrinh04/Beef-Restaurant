#!/bin/bash

# Stop on error
set -e

# Variable Definitions
REPO_PREFIX="minh28012004"
TAG="latest"

echo "============================================"
echo "    BEEF RESTAURANT - BUILD & PUSH SCRIPT   "
echo "============================================"

# Ensure Docker is logged in
if ! docker system info > /dev/null 2>&1; then
  echo "Error: Docker is not running or you don't have permission."
  exit 1
fi

echo "[1/3] Building Backend Services..."

# Build Menu Service (Multi-stage handles BuildingBlocks)
echo "--- Building Menu Service ---"
docker build --network=host -t $REPO_PREFIX/menu-service:$TAG -f Backend/MenuService/Dockerfile Backend

echo "--- Building Basket Service ---"
docker build --network=host -t $REPO_PREFIX/basket-service:$TAG -f Backend/BasketService/Dockerfile Backend

echo "--- Building Ordering Service ---"
docker build --network=host -t $REPO_PREFIX/ordering-service:$TAG -f Backend/OrderingService/Dockerfile Backend

echo "--- Building Payment Service ---"
docker build --network=host -t $REPO_PREFIX/payment-service:$TAG -f Backend/PaymentService/Dockerfile Backend

echo "--- Building User Service ---"
docker build --network=host -t $REPO_PREFIX/user-service:$TAG -f Backend/UserService/Dockerfile Backend

echo "--- Building Gateway Service ---"
docker build --network=host -t $REPO_PREFIX/gateway-service:$TAG -f Backend/GatewayService/Dockerfile Backend

echo "--- Building Discovery Service ---"
docker build --network=host -t $REPO_PREFIX/discovery-service:$TAG -f Backend/DiscoveryService/Dockerfile Backend


echo "[2/3] Building Frontend Applications..."

# Define Build Args for K8s environment
API_BASE_URL="https://api.beef.local"
AUTH_URL="https://auth.beef.local"
APP_URL="https://app.beef.local"

# Frontend (Next.js)
echo "--- Building Frontend (NextJS) ---"
# Note: NextJS uses NEXT_PUBLIC_ during build.
docker build --network=host \
  --build-arg NEXT_PUBLIC_API_BASE=$API_BASE_URL \
  -t $REPO_PREFIX/frontend:$TAG Frontend

# Admin (Vite)
echo "--- Building Admin FE (Vite) ---"
# Note: Vite replaces VITE_ vars during build.
docker build --network=host \
  --build-arg VITE_API_BASE_URL=$API_BASE_URL \
  --build-arg VITE_KEYCLOAK_URL=$AUTH_URL \
  --build-arg VITE_KEYCLOAK_REALM=master \
  --build-arg VITE_KEYCLOAK_CLIENT_ID=test-user \
  -t $REPO_PREFIX/fe-admin:$TAG fe-admin


echo "[3/3] Pushing Images to Docker Hub..."
echo "Make sure you have run 'docker login' before script!"

docker push $REPO_PREFIX/menu-service:$TAG
docker push $REPO_PREFIX/basket-service:$TAG
docker push $REPO_PREFIX/ordering-service:$TAG
docker push $REPO_PREFIX/payment-service:$TAG
docker push $REPO_PREFIX/user-service:$TAG
docker push $REPO_PREFIX/gateway-service:$TAG
docker push $REPO_PREFIX/discovery-service:$TAG
docker push $REPO_PREFIX/frontend:$TAG
docker push $REPO_PREFIX/fe-admin:$TAG

echo "============================================"
echo "    BUILD & PUSH COMPLETED SUCCESSFULLY     "
echo "============================================"
