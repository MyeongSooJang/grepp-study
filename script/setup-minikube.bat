@echo off
setlocal enabledelayedexpansion

echo [1/5] Chocolatey 확인
where choco >nul 2>nul
if errorlevel 1 (
    echo Missing command: choco
    echo Install Chocolatey first: https://chocolatey.org/install
    exit /b 1
)

echo [2/5] minikube / kubectl 설치
where minikube >nul 2>nul
if errorlevel 1 (
    choco install -y minikube
)

where kubectl >nul 2>nul
if errorlevel 1 (
    choco install -y kubernetes-cli
)

echo [3/5] Docker 확인
where docker >nul 2>nul
if errorlevel 1 (
    echo Missing command: docker
    echo Install Docker Desktop first: https://www.docker.com/products/docker-desktop/
    exit /b 1
)

docker info >nul 2>nul
if errorlevel 1 (
    echo Docker daemon is not running. Start Docker Desktop and retry.
    exit /b 1
)

echo [4/5] Minikube 시작
set MINIKUBE_ARGS=start --driver=docker

if not "%MINIKUBE_CPUS%"=="" (
    set MINIKUBE_ARGS=!MINIKUBE_ARGS! --cpus=%MINIKUBE_CPUS%
)

if not "%MINIKUBE_MEMORY%"=="" (
    set MINIKUBE_ARGS=!MINIKUBE_ARGS! --memory=%MINIKUBE_MEMORY%
)

call minikube !MINIKUBE_ARGS!
if errorlevel 1 (
    exit /b 1
)

echo [5/5] 클러스터 확인
call kubectl cluster-info
if errorlevel 1 exit /b 1

call kubectl get nodes
if errorlevel 1 exit /b 1

call kubectl get pods -A
if errorlevel 1 exit /b 1

echo Minikube is ready.
exit /b 0
