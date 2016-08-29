#!/usr/bin/env bash

# set -x

MINIKUBE=$(which minikube)
KUBECTL=$(which kubectl)

start_minikube() {
  if [ $(${MINIKUBE} status) == "Running" ]
    then
      echo "Minikube is already running"
      return 1
  fi

  echo "Starting minikube..."
  ${MINIKUBE} start
  return 0
}

start_minikube

${KUBECTL} create -f poc-product-service.yaml

${KUBECTL} expose deployment poc-product-service --type=NodePort

${MINIKUBE} service poc-product-service --url