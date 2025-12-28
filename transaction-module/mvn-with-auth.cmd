@echo off
setlocal ENABLEDELAYEDEXPANSION

:: Configura a cor do terminal (opcional)
color 0A

:: Arte ASCII
echo   ____             _       _
echo  |  _ \ _ __ _   _| |_ ___| | __
echo  | |_) | '__| | | | __/ _ \ |/ /
echo  |  __/| |  | |_| | ||  __/   <
echo  |_|   |_|   \__,_|\__\___|_|\_\
echo.

:: Verifica se o arquivo settings.xml existe
if not exist settings.xml (
    echo ⚠️  Aviso: O arquivo settings.xml não foi encontrado no diretório atual.
    echo 🛠  Criando um arquivo padrão settings.xml...
    type nul > settings.xml
    echo ✅ Arquivo settings.xml criado com sucesso.
) else (
    echo ✅ Arquivo settings.xml encontrado.
)

:: Verifica se Java 17 está instalado
echo 🔍 Verificando se Java 17 está realmente instalado...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java não encontrado no PATH. Por favor, instale o JDK 17 manualmente.
    pause
    exit /b 1
) else (
    for /f "tokens=2 delims== " %%i in ('java -version 2^>^&1 ^| findstr "version"') do (
        set "javaversion=%%~i"
    )

    echo Detected Java version: !javaversion!

    echo !javaversion! | findstr "17." >nul
    if %errorlevel% neq 0 (
        echo ❌ Java encontrado, mas não é a versão 17. Por favor, instale o JDK 17.
        pause
        exit /b 1
    ) else (
        echo ✅ Java 17 detectado no PATH do sistema.
    )
)

:: Define JAVA_HOME se ainda não estiver definido
if not defined JAVA_HOME (
    for /f "delims=" %%i in ('where java') do set "JAVA_HOME=%%~dpi.."
    echo ✅ JAVA_HOME definido para: %JAVA_HOME%
)

:: Exibe a versão do Java usando JAVA_HOME
echo 🔍 Verificando a versão do Java usando JAVA_HOME...
"%JAVA_HOME%\bin\java" -version

:: Executa o Maven com as configurações locais
echo 🚀 Executando Maven com as configurações locais...
mvn -s settings.xml %*

pause
