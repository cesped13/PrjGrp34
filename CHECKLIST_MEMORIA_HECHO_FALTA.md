# Checklist 

## Evidencias encontradas en el repo
- Documento de memoria: 02_2526_MemoriaGrp34.docx
- README: README.md (solo título del proyecto)
- Diagramas: diseño/diagramaclases.uml, diseño/diagramaER.uml, diseño/DiagramaClases.png, diseño/DiagramaER.png
- Rama actual: main
- Tags: ninguno
- Código de aplicación: no encontrado
- Pruebas automáticas: no encontradas
- Configuración de build (Maven/Gradle): no encontrada

## 1) Diseño del código
- Estado: Parcial
- Hecho:
  - Modelado visual disponible (ER y clases) en carpeta diseño.
- Falta:
  - Implementación de clases de dominio (Maquina, Localizacion, Producto, StockMaquina).
  - Implementación de DAOs en memoria (MaquinaDAO, ProductoDAO, StockDAO).
  - Reglas y validaciones con IllegalArgumentException en código real.
  - Lógica de reposición y fecha de agotamiento en StockMaquina.
- Criterio de cierre:
  - Existe código compilable que implementa el modelo y DAOs según memoria.

## 2) Restricciones de construcción
- Estado: No verificable / pendiente
- Hecho:
  - No hay indicios de persistencia externa en el repo actual.
- Falta:
  - Confirmar en código que no hay BD/ficheros como persistencia de negocio.
  - Confirmar ausencia de main funcional y pruebas exclusivamente con JUnit.
- Criterio de cierre:
  - El proyecto ejecuta tests JUnit sin requerir main ni persistencia externa.

## 3) Plan de pruebas
- Estado: Pendiente
- Hecho:
  - La memoria describe estrategia general (CN/CB, CE/McCabe, enfoque bottom-up).
- Falta:
  - Trazabilidad explícita historia -> funcionalidad -> clase -> método a probar.
  - Sección de "características que no se prueban" concreta para vuestro caso.
  - Entorno de pruebas con versiones (IDE, JUnit, Mockito, cobertura).
  - Riesgos y tiempos aterrizados con datos del equipo.
- Criterio de cierre:
  - Plan completo con tablas/listados específicos del proyecto y no solo texto guía.

## 4) Diseño de pruebas (detalle)
- Estado: Pendiente
- Hecho:
  - Plantilla/estructura en la memoria.
- Falta:
  - Definición real de pruebas y subpruebas por método.
  - CP de Caja Negra por CE documentados y codificados.
  - CP de Caja Blanca (McCabe) cuando no haya cobertura de decisión.
  - Referencias a anexos con justificación técnica.
- Criterio de cierre:
  - Cada prueba tiene objetivo, método(s), CP, paso/fallo y evidencia de ejecución.

## 5) Informe de pruebas (máx. 4 páginas)
- Estado: Pendiente
- Hecho:
  - No se encontró informe en el repo.
- Falta:
  - Introducción, metodología, resultados, conclusiones y limitaciones.
  - Métricas: % pruebas pasadas, fallos por versión, cobertura, corregidos.
  - Gráficas/tabla de resultados.
- Criterio de cierre:
  - Informe ejecutivo final con datos reales de ejecución de tests.

## 6) Gestión en GitHub (repositorio, board, tags)
- Estado: Pendiente crítico
- Hecho:
  - Repo creado y en rama main.
- Falta:
  - Código y pruebas en el repositorio.
  - Al menos 1 tag por sprint (actualmente 0).
  - Evidencia de issues/historias y flujo Kanban por columnas indicadas.
  - Evidencia de commits previos al paso de historias a hecho.
- Criterio de cierre:
  - Se verifican tags, issues, columnas y commits alineados con la memoria.

## 7) Entregables
- Estado: Pendiente
- Hecho:
  - Existe el documento de memoria.
- Falta:
  - ZIP de proyecto finalizado.
  - Documentación final consolidada.
  - Enlaces/acceso a repo y Kanban para revisión docente.
- Criterio de cierre:
  - Paquete final completo y verificable por tercero.

## 8) Retrospectivas y políticas
- Estado: Pendiente
- Hecho:
  - La memoria incluye propuesta inicial de política de configuración.
- Falta:
  - Registro de retrospectivas por fecha y acuerdos reales.
  - Ajustes de política tras cada revisión.
- Criterio de cierre:
  - Historial de retrospectivas y decisiones aplicadas al proceso.

---

## Prioridad inmediata recomendada (orden de ejecución)
1. Crear estructura de proyecto (src/test + dependencias JUnit/Mockito).
2. Implementar modelo y DAOs en memoria.
3. Escribir pruebas de aceptación de Sprint 1 y luego unidad/integración.
4. Medir cobertura y completar con McCabe si falta cobertura de decisión.
5. Crear/ordenar issues y tablero Kanban según columnas de la memoria.
6. Etiquetar sprint con tag y cerrar informe ejecutivo + anexos.
