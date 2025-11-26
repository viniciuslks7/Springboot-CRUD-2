# Script para testar PostgreSQL e executar aplicação
# Execute este script no PowerShell

Write-Host "🔍 Testando conexão com PostgreSQL..."

try {
    # Testar conexão usando .NET
    $connectionString = "Server=localhost;Port=5432;User Id=postgres;Password=123456;Database=postgres;"
    $connection = New-Object Npgsql.NpgsqlConnection($connectionString)

    $connection.Open()
    Write-Host "✅ Conexão com PostgreSQL estabelecida!"

    # Verificar se banco oficina3d existe
    $command = $connection.CreateCommand()
    $command.CommandText = "SELECT datname FROM pg_database WHERE datname = 'oficina3d';"
    $reader = $command.ExecuteReader()

    $databaseExists = $reader.Read()
    $reader.Close()

    if (-not $databaseExists) {
        Write-Host "📦 Criando banco de dados oficina3d..."
        $command.CommandText = "CREATE DATABASE oficina3d;"
        $command.ExecuteNonQuery()
        Write-Host "✅ Banco oficina3d criado!"
    } else {
        Write-Host "✅ Banco oficina3d já existe!"
    }

    $connection.Close()
} catch {
    Write-Host "❌ Erro ao conectar com PostgreSQL: $($_.Exception.Message)"
    Write-Host "Verifique se o PostgreSQL está instalado e rodando."
    exit 1
}

Write-Host "🚀 Iniciando aplicação Spring Boot..."
Set-Location "C:\Users\LAB\Desktop\Spring MVC"
.\mvnw spring-boot:run