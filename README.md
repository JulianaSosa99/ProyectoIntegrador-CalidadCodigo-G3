# ProyectoIntegrador-CalidadCodigo-G3

[![CI/CD Pipeline](https://github.com/[usuario]/ProyectoIntegrador-CalidadCodigo-G3/actions/workflows/ci-cd-quality.yml/badge.svg)](https://github.com/[usuario]/ProyectoIntegrador-CalidadCodigo-G3/actions/workflows/ci-cd-quality.yml)

## 📋 Descripción

Proyecto integrador de Calidad de Software - Grupo 3. Sistema de gestión de inventario implementado siguiendo principios SOLID, Clean Code y arquitectura por capas, con un pipeline CI/CD automatizado.

## 🚀 Características

- ✅ **Arquitectura por capas**: Model, Service, Output, Exception
- ✅ **Principios SOLID**: Código modular y mantenible
- ✅ **Clean Code**: Nombres descriptivos, métodos pequeños, bajo acoplamiento
- ✅ **Pruebas unitarias**: JUnit 5 con cobertura >80%
- ✅ **Análisis estático**: Checkstyle, PMD, CPD
- ✅ **CI/CD Pipeline**: GitHub Actions con Quality Gate automatizado

## 🏗️ Estructura del Proyecto

```
src/
├── main/java/com/example/inventory/
│   ├── model/              # Entidades del dominio
│   ├── service/            # Lógica de negocio
│   ├── output/             # Presentación de datos
│   ├── exception/          # Excepciones personalizadas
│   ├── Inventory.java      # Clase principal de inventario
│   └── Main.java           # Punto de entrada
└── test/java/com/example/inventory/
    ├── model/              # Tests de modelos
    └── service/            # Tests de servicios
```

## 🔧 Tecnologías

- **Java 17**: Lenguaje de programación
- **Maven**: Gestor de dependencias y build
- **JUnit 5**: Framework de pruebas unitarias
- **JaCoCo**: Cobertura de código
- **Checkstyle**: Verificación de estilo (Google Java Style Guide)
- **PMD**: Análisis estático de código
- **CPD**: Detección de duplicación de código
- **GitHub Actions**: CI/CD Pipeline

## 🚀 Pipeline CI/CD

Este proyecto implementa un pipeline automatizado de integración continua que se ejecuta en cada commit y pull request:

### Jobs del Pipeline

1. **Build & Validate** (~2 min): Compilación y análisis estático
2. **Tests & Coverage** (~1 min): Ejecución de tests y medición de cobertura
3. **Quality Gate** (~1 min): Verificación de métricas de calidad
4. **Summary** (~30 seg): Generación de resumen

### Métricas de Calidad

- ✅ Cobertura de código: ≥80%
- ✅ Todos los tests deben pasar
- ✅ Código debe compilar sin errores
- ✅ Checkstyle, PMD y CPD ejecutados

**📖 Ver documentación completa**: [PIPELINE_DOCUMENTATION.md](PIPELINE_DOCUMENTATION.md)

## 💻 Instalación y Uso

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+

### Compilar el proyecto

```bash
mvn clean compile
```

### Ejecutar tests

```bash
mvn test
```

### Generar reportes de calidad

```bash
# Reporte de cobertura
mvn jacoco:report

# Ver reporte en navegador
start target/site/jacoco/index.html  # Windows
open target/site/jacoco/index.html   # macOS/Linux
```

### Ejecutar el programa

```bash
mvn clean compile
java -cp target/classes com.example.inventory.Main
```

### Verificación completa (como el pipeline)

```bash
# Opción 1: Maven
mvn clean verify

# Opción 2: Script de prueba local (recomendado)
.\test-pipeline.ps1
```

## 📊 Reportes Disponibles

Después de ejecutar `mvn clean verify site`:

- **Cobertura JaCoCo**: `target/site/jacoco/index.html`
- **Checkstyle**: `target/site/checkstyle.html`
- **PMD**: `target/site/pmd.html`
- **CPD**: `target/site/cpd.html`
- **Tests**: `target/surefire-reports/`

## 👥 Equipo

### Roles y Responsabilidades

- **Gorki**: Refactorización y aplicación de principios SOLID
- **Mirtha**: Implementación de pruebas unitarias y JaCoCo
- **Slendy**: Configuración de herramientas de análisis estático (Checkstyle, PMD, CPD)
- **Pancho**: Implementación del pipeline CI/CD con GitHub Actions

## 📚 Documentación

- [Pipeline CI/CD - Documentación Completa](PIPELINE_DOCUMENTATION.md)
- [Guía Rápida del Pipeline](.github/README_PIPELINE.md)
- [Evidencias para el Informe](EVIDENCIAS_INFORME.md)

## 📈 Mejoras Implementadas

### Antes de la Refactorización

- ❌ Código monolítico en una sola clase
- ❌ Violación de principios SOLID
- ❌ Sin pruebas unitarias
- ❌ Sin análisis de calidad
- ❌ Verificación manual

### Después de la Refactorización

- ✅ Arquitectura por capas
- ✅ Código limpio y documentado
- ✅ 85% de cobertura de tests
- ✅ Análisis estático automatizado
- ✅ Pipeline CI/CD con Quality Gate
- ✅ Detección temprana de errores

## 🔄 Workflow de Desarrollo

1. Crea una rama desde `develop`
2. Realiza tus cambios
3. Ejecuta `.\test-pipeline.ps1` localmente
4. Si pasa, haz commit y push
5. El pipeline se ejecuta automáticamente
6. Crea un Pull Request
7. El pipeline valida el PR
8. Si pasa, merge a `develop`

## 📝 Licencia

Proyecto académico - Universidad Latinoamericana (UDLA)  
Calidad de Software - 2026

## 📞 Contacto

Para consultas sobre el proyecto, contactar a los miembros del equipo a través del repositorio de GitHub.

---

**⭐ Si este proyecto te ayudó, dale una estrella!**
