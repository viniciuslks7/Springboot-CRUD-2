# Script simples para verificar PostgreSQL e executar aplicação
# Execute: .\setup-and-run.ps1

Write-Host "=== VERIFICACAO POSTGRESQL ===" -ForegroundColor Cyan

# Verificar serviço
$service = Get-Service -Name "postgresql-x64-16" -ErrorAction SilentlyContinue
if (-not $service) {
    Write-Host "ERRO: Servico postgresql-x64-16 nao encontrado!" -ForegroundColor Red
    Write-Host "Verifique se PostgreSQL 16 esta instalado." -ForegroundColor Yellow
    exit 1
}

if ($service.Status -ne "Running") {
    Write-Host "ERRO: PostgreSQL nao esta rodando!" -ForegroundColor Red
    Write-Host "Inicie o servico: Start-Service postgresql-x64-16" -ForegroundColor Yellow
    exit 1
}

Write-Host "OK: PostgreSQL esta rodando!" -ForegroundColor Green

# Instruções para criar banco
Write-Host "`n=== CRIAR BANCO DE DADOS ===" -ForegroundColor Cyan
Write-Host "Execute este comando no pgAdmin ou psql:" -ForegroundColor Yellow
Write-Host "CREATE DATABASE oficina3d OWNER postgres ENCODING 'UTF8';" -ForegroundColor White
Write-Host ""
Write-Host "Ou use o arquivo: create_db_simple.sql" -ForegroundColor Yellow

# Perguntar se banco foi criado
$response = Read-Host "Banco criado? (s/n)"
if ($response -ne "s" -and $response -ne "S") {
    Write-Host "Crie o banco primeiro, depois execute novamente." -ForegroundColor Yellow
    exit 0
}

# Executar aplicação
Write-Host "`n=== INICIANDO APLICACAO ===" -ForegroundColor Green
Write-Host "Acesse: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Login: user / Senha: (veja no log)" -ForegroundColor Cyan

.\mvnw spring-boot:run