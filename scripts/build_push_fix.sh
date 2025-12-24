#!/bin/bash

# Stop on error
set -e

# Variable Definitions
REPO_PREFIX="minh28012004"
TAG="latest"

echo "============================================"
echo "    BEEF RESTAURANT - QUICK FIX BUILD       "
echo "    (User, Menu, Ordering Services ONLY)    "
echo "============================================"

# Ensure Docker is logged in
if ! docker system info > /dev/null 2>&1; then
  echo "Error: Docker is not running or you don't have permission."
  exit 1
fi

echo "[1/1] Building Services with Code Changes..."

# Build Menu Service
echo "--- Building Menu Service ---"
docker build --network=host -t $REPO_PREFIX/menu-service:$TAG -f Backend/MenuService/Dockerfile Backend

# Build Ordering Service
echo "--- Building Ordering Service ---"
docker build --network=host -t $REPO_PREFIX/ordering-service:$TAG -f Backend/OrderingService/Dockerfile Backend

# Build User Service
echo "--- Building User Service ---"
docker build --network=host -t $REPO_PREFIX/user-service:$TAG -f Backend/UserService/Dockerfile Backend

echo "[2/2] Pushing Images to Docker Hub..."

docker push $REPO_PREFIX/menu-service:$TAG
docker push $REPO_PREFIX/ordering-service:$TAG
docker push $REPO_PREFIX/user-service:$TAG

echo "============================================"
echo "    QUICK FIX BUILD & PUSH COMPLETED        "
echo "============================================"
