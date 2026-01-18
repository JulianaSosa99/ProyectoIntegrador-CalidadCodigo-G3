Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Pipeline CI/CD - Prueba Local" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$ErrorCount = 0
$WarningCount = 0
$StartTime = Get-Date

if (-not (Test-Path "pom.xml")) {
    Write-Host "[ERROR] No se encuentra pom.xml" -ForegroundColor Red
    Write-Host "   Ejecuta este script desde la raiz del proyecto" -ForegroundColor Yellow
    exit 1
}

Write-Host "Directorio de trabajo: $PWD" -ForegroundColor Gray
Write-Host ""

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "JOB 1: Build & Validate" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

Write-Host "Verificando Java..." -ForegroundColor Cyan
try {
    $JavaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "   [OK] Java instalado: $JavaVersion" -ForegroundColor Green
} catch {
    Write-Host "   [FAIL] Java no encontrado" -ForegroundColor Red
    $ErrorCount++
}
Write-Host ""

Write-Host "Verificando Maven..." -ForegroundColor Cyan
try {
    $MavenVersion = mvn -version | Select-String "Apache Maven" | Select-Object -First 1
    Write-Host "   [OK] Maven instalado: $MavenVersion" -ForegroundColor Green
} catch {
    Write-Host "   [FAIL] Maven no encontrado" -ForegroundColor Red
    $ErrorCount++
    Write-Host "   Instala Maven desde: https://maven.apache.org/" -ForegroundColor Yellow
    exit 1
}
Write-Host ""

Write-Host "Compilando proyecto..." -ForegroundColor Cyan
$CompileStart = Get-Date
mvn clean compile -q -B
if ($LASTEXITCODE -eq 0) {
    $CompileTime = ((Get-Date) - $CompileStart).TotalSeconds
    Write-Host "   [OK] Compilacion exitosa (${CompileTime}s)" -ForegroundColor Green
} else {
    Write-Host "   [FAIL] Compilacion fallida" -ForegroundColor Red
    $ErrorCount++
    Write-Host ""
    Write-Host "Ejecuta 'mvn clean compile' para ver los errores detallados" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "Ejecutando Checkstyle..." -ForegroundColor Cyan
mvn checkstyle:checkstyle -q -B
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] Checkstyle completado" -ForegroundColor Green
    if (Test-Path "target/checkstyle-result.xml") {
        $CheckstyleContent = Get-Content "target/checkstyle-result.xml" -Raw
        $Violations = ([regex]::Matches($CheckstyleContent, '<error')).Count
        if ($Violations -gt 0) {
            Write-Host "   [WARNING] $Violations violaciones encontradas" -ForegroundColor Yellow
            $WarningCount++
        } else {
            Write-Host "   [OK] Sin violaciones" -ForegroundColor Green
        }
    }
} else {
    Write-Host "   [WARNING] Checkstyle con warnings" -ForegroundColor Yellow
    $WarningCount++
}
Write-Host ""

Write-Host "Ejecutando PMD..." -ForegroundColor Cyan
mvn pmd:pmd -q -B
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] PMD completado" -ForegroundColor Green
    if (Test-Path "target/pmd.xml") {
        $PMDContent = Get-Content "target/pmd.xml" -Raw
        $PMDViolations = ([regex]::Matches($PMDContent, '<violation')).Count
        if ($PMDViolations -gt 0) {
            Write-Host "   [WARNING] $PMDViolations violaciones encontradas" -ForegroundColor Yellow
            $WarningCount++
        } else {
            Write-Host "   [OK] Sin violaciones" -ForegroundColor Green
        }
    }
} else {
    Write-Host "   [WARNING] PMD con warnings" -ForegroundColor Yellow
    $WarningCount++
}
Write-Host ""

Write-Host "Ejecutando CPD (Copy-Paste Detector)..." -ForegroundColor Cyan
mvn pmd:cpd -q -B
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] CPD completado" -ForegroundColor Green
    if (Test-Path "target/cpd.xml") {
        $CPDContent = Get-Content "target/cpd.xml" -Raw
        $Duplications = ([regex]::Matches($CPDContent, '<duplication')).Count
        if ($Duplications -gt 0) {
            Write-Host "   [WARNING] $Duplications bloques duplicados encontrados" -ForegroundColor Yellow
            $WarningCount++
        } else {
            Write-Host "   [OK] Sin duplicaciones" -ForegroundColor Green
        }
    }
} else {
    Write-Host "   [WARNING] CPD con warnings" -ForegroundColor Yellow
    $WarningCount++
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "JOB 2: Tests & Coverage" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

Write-Host "Ejecutando tests unitarios..." -ForegroundColor Cyan
$TestStart = Get-Date
mvn test -q -B
if ($LASTEXITCODE -eq 0) {
    $TestTime = ((Get-Date) - $TestStart).TotalSeconds
    Write-Host "   [OK] Tests exitosos (${TestTime}s)" -ForegroundColor Green
    
    if (Test-Path "target/surefire-reports") {
        $TestFiles = Get-ChildItem "target/surefire-reports/TEST-*.xml" -ErrorAction SilentlyContinue
        $TestCount = $TestFiles.Count
        Write-Host "   [INFO] $TestCount archivos de test ejecutados" -ForegroundColor Cyan
    }
} else {
    Write-Host "   [FAIL] Tests fallidos" -ForegroundColor Red
    $ErrorCount++
    Write-Host ""
    Write-Host "Ejecuta 'mvn test' para ver los errores detallados" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "Generando reporte de cobertura..." -ForegroundColor Cyan
mvn jacoco:report -q -B
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] Reporte JaCoCo generado" -ForegroundColor Green
    
    if (Test-Path "target/site/jacoco/jacoco.csv") {
        $Coverage = Import-Csv "target/site/jacoco/jacoco.csv"
        $TotalInstructionsMissed = ($Coverage | Measure-Object -Property INSTRUCTION_MISSED -Sum).Sum
        $TotalInstructionsCovered = ($Coverage | Measure-Object -Property INSTRUCTION_COVERED -Sum).Sum
        $TotalInstructions = $TotalInstructionsMissed + $TotalInstructionsCovered
        
        if ($TotalInstructions -gt 0) {
            $CoveragePercent = [math]::Round(($TotalInstructionsCovered / $TotalInstructions) * 100, 2)
            
            if ($CoveragePercent -ge 80) {
                Write-Host "   [OK] Cobertura: $CoveragePercent% (objetivo: >=80%)" -ForegroundColor Green
            } elseif ($CoveragePercent -ge 70) {
                Write-Host "   [WARNING] Cobertura: $CoveragePercent% (objetivo: >=80%)" -ForegroundColor Yellow
                $WarningCount++
            } else {
                Write-Host "   [FAIL] Cobertura: $CoveragePercent% (objetivo: >=80%)" -ForegroundColor Red
                $ErrorCount++
            }
        }
    }
    
    Write-Host "   [INFO] Reporte HTML: target/site/jacoco/index.html" -ForegroundColor Gray
} else {
    Write-Host "   [WARNING] Error al generar reporte" -ForegroundColor Yellow
    $WarningCount++
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "JOB 3: Quality Gate" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow
Write-Host ""

Write-Host "Ejecutando verificacion completa..." -ForegroundColor Cyan
mvn verify -q -B -DskipTests
if ($LASTEXITCODE -eq 0) {
    Write-Host "   [OK] Verificacion exitosa" -ForegroundColor Green
} else {
    Write-Host "   [WARNING] Verificacion con warnings" -ForegroundColor Yellow
    $WarningCount++
}
Write-Host ""

Write-Host "Verificando Quality Gate..." -ForegroundColor Cyan
$QualityGatePassed = $true

if (Test-Path "target/classes") {
    Write-Host "   [OK] Compilacion: OK" -ForegroundColor Green
} else {
    Write-Host "   [FAIL] Compilacion: FAIL" -ForegroundColor Red
    $QualityGatePassed = $false
}

if (Test-Path "target/surefire-reports") {
    $TestFiles = Get-ChildItem "target/surefire-reports/TEST-*.xml" -ErrorAction SilentlyContinue
    if ($TestFiles.Count -gt 0) {
        Write-Host "   [OK] Tests: $($TestFiles.Count) ejecutados" -ForegroundColor Green
    } else {
        Write-Host "   [FAIL] Tests: No se encontraron resultados" -ForegroundColor Red
        $QualityGatePassed = $false
    }
} else {
    Write-Host "   [FAIL] Tests: Directorio no encontrado" -ForegroundColor Red
    $QualityGatePassed = $false
}

if (Test-Path "target/checkstyle-result.xml") {
    Write-Host "   [OK] Checkstyle: Ejecutado" -ForegroundColor Green
} else {
    Write-Host "   [WARNING] Checkstyle: No ejecutado" -ForegroundColor Yellow
}

if (Test-Path "target/pmd.xml") {
    Write-Host "   [OK] PMD: Ejecutado" -ForegroundColor Green
} else {
    Write-Host "   [WARNING] PMD: No ejecutado" -ForegroundColor Yellow
}

if (Test-Path "target/site/jacoco/jacoco.xml") {
    Write-Host "   [OK] JaCoCo: Ejecutado" -ForegroundColor Green
} else {
    Write-Host "   [WARNING] JaCoCo: No ejecutado" -ForegroundColor Yellow
}

Write-Host ""

$EndTime = Get-Date
$TotalTime = ($EndTime - $StartTime).TotalSeconds

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "RESUMEN DEL PIPELINE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Tiempo total: ${TotalTime}s (~$([math]::Round($TotalTime/60, 1)) minutos)" -ForegroundColor Gray
Write-Host "Errores: $ErrorCount" -ForegroundColor $(if ($ErrorCount -eq 0) { "Green" } else { "Red" })
Write-Host "Warnings: $WarningCount" -ForegroundColor $(if ($WarningCount -eq 0) { "Green" } else { "Yellow" })
Write-Host ""

if ($QualityGatePassed -and $ErrorCount -eq 0) {
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "[PASSED] QUALITY GATE: PASSED" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Tu codigo esta listo para commit!" -ForegroundColor Green
    Write-Host ""
    
    if ($WarningCount -gt 0) {
        Write-Host "Tip: Tienes $WarningCount warnings. Considera resolverlos para mejorar la calidad." -ForegroundColor Yellow
    }
} else {
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "[FAILED] QUALITY GATE: FAILED" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Por favor, corrige los errores antes de hacer commit." -ForegroundColor Red
    Write-Host ""
}

Write-Host "REPORTES GENERADOS:" -ForegroundColor Cyan
Write-Host ""
if (Test-Path "target/site/jacoco/index.html") {
    Write-Host "   Cobertura JaCoCo:  target\site\jacoco\index.html" -ForegroundColor Gray
}
if (Test-Path "target/checkstyle-result.xml") {
    Write-Host "   Checkstyle:        target\checkstyle-result.xml" -ForegroundColor Gray
}
if (Test-Path "target/pmd.xml") {
    Write-Host "   PMD:               target\pmd.xml" -ForegroundColor Gray
}
if (Test-Path "target/cpd.xml") {
    Write-Host "   CPD:               target\cpd.xml" -ForegroundColor Gray
}
if (Test-Path "target/surefire-reports") {
    Write-Host "   Tests:             target\surefire-reports\" -ForegroundColor Gray
}
Write-Host ""

Write-Host "Deseas abrir el reporte de cobertura en el navegador? (S/N): " -NoNewline -ForegroundColor Cyan
$Response = Read-Host
if ($Response -eq "S" -or $Response -eq "s") {
    if (Test-Path "target/site/jacoco/index.html") {
        Start-Process "target\site\jacoco\index.html"
        Write-Host "[OK] Abriendo reporte en el navegador..." -ForegroundColor Green
    } else {
        Write-Host "[ERROR] No se encontro el reporte" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Pipeline local completado" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

if ($QualityGatePassed -and $ErrorCount -eq 0) {
    exit 0
} else {
    exit 1
}
