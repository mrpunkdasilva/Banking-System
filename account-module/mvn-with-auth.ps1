# Função para imprimir texto colorido
function Print-Color {
    param (
        [string]$Color,
        [string]$Text
    )
    $colorMap = @{
        "Red" = 31
        "Green" = 32
        "Yellow" = 33
        "Blue" = 34
        "Magenta" = 35
        "Cyan" = 36
    }
    Write-Host $Text -ForegroundColor $Color
}

# Arte ASCII
function Print-AsciiArt {
    Print-Color -Color "Magenta" -Text "  ____             _       _    "
    Print-Color -Color "Magenta" -Text " |  _ \ _ __ _   _| |_ ___| | __"
    Print-Color -Color "Magenta" -Text " | |_) | '__| | | | __/ _ \ |/ /"
    Print-Color -Color "Magenta" -Text " |  __/| |  | |_| | ||  __/   < "
    Print-Color -Color "Magenta" -Text " |_|   |_|   \__,_|\__\___|_|\_\\"
    Print-Color -Color "Magenta" -Text "                                "
}

Set-ExecutionPolicy RemoteSigned

# Exibea arte ASCII
Print-AsciiArt

# Verifica se o arquivo settings.xml existe, caso contrário, cria um
if (-Not (Test-Path -Path "settings.xml")) {
    Print-Color -Color "Red" -Text "⚠️  Aviso: O arquivo settings.xml não foi encontrado no diretório atual."
    Print-Color -Color "Red" -Text "🛠  Criando um arquivo padrão settings.xml..."
    New-Item -Path "settings.xml" -ItemType File
    Print-Color -Color "Green" -Text "✅ Arquivo settings.xml criado com sucesso."
} else {
    Print-Color -Color "Green" -Text "✅ Arquivo settings.xml encontrado."
}

# Função para instalar o JDK 17
function Install-JDK17 {
    Print-Color -Color "Yellow" -Text "⚠️  Java 17 não está instalado. Iniciando a instalação do JDK 17..."
    # Baixar e instalar o JDK 17
    $jdkUrl = "https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe"
    $installerPath = "$env:TEMP\jdk-17_installer.exe"
    Invoke-WebRequest -Uri $jdkUrl -OutFile $installerPath
    Start-Process -FilePath $installerPath -ArgumentList "/s" -Wait
    Remove-Item -Path $installerPath
    Print-Color -Color "Green" -Text "✅ JDK 17 instalado com sucesso."
}

# Verifica se o Java 17 está instalado
$javaVersion = & java -version 2>&1
if ($javaVersion -notmatch "17") {
    Install-JDK17
} else {
    Print-Color -Color "Green" -Text "✅ Java 17 já está instalado."
}

# Define o JAVA_HOME para usar o Java 17
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Exibe a versão do Java
Print-Color -Color "Blue" -Text "🔍 Verificando a versão do Java..."
& "$env:JAVA_HOME\bin\java.exe" -version

# Executa o Maven com as configurações locais
Print-Color -Color "Cyan" -Text "🚀 Executando Maven com as configurações locais..."
& mvn -s settings.xml @args