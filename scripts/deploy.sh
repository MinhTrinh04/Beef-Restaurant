#!/bin/bash

# Stop on error
set -e

echo "============================================"
echo "    BEEF RESTAURANT - DEPLOY SCRIPT         "
echo "============================================"

# Function to check if a resource exists
check_ns() {
  kubectl get ns beef-restaurant > /dev/null 2>&1
}

if ! check_ns; then
    echo "Creating Namespace..."
    kubectl apply -f k8s/00-namespace.yaml
else
    echo "Namespace 'beef-restaurant' already exists."
fi

echo "[1/4] Applying Secrets & ConfigMaps..."
kubectl apply -f k8s/00-secrets.yaml
# (If ConfigMap is in separate file, apply here. Currently in postgres.yaml)

echo "[2/4] Deploying Infrastructure..."
kubectl apply -f k8s/01-infrastructure/

echo "Waiting for Infrastructure (Postgres/RabbitMQ) to be ready..."
sleep 10 # Give a small buffer
# Optional: Wait for postgres
kubectl wait --for=condition=ready pod -l app=postgres -n beef-restaurant --timeout=120s || echo "Postgres not yet ready, continuing..."

echo "[3/4] Deploying Microservices..."
kubectl apply -f k8s/02-services/

echo "[4/4] Deploying Frontends & Ingress..."
kubectl apply -f k8s/03-frontend/
kubectl apply -f k8s/04-ingress/

# Tunnel is optional, only if token is set
if [ -f "k8s/05-tunnel/cloudflared.yaml" ]; then
    echo "[Optional] Deploying Cloudflare Tunnel..."
    kubectl apply -f k8s/05-tunnel/cloudflared.yaml
fi

echo "============================================"
echo "    DEPLOYMENT COMMANDS SENT                "
echo "    Check status with: kubectl get pods -n beef-restaurant"
echo "============================================"
