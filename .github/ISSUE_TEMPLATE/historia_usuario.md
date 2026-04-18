## Identificación

| Campo | Valor |
|---|---|
| **ID Historia** | HU-XX |
| **Sprint** | Sprint 1 / Sprint 2 |
| **Nivel de prueba** | Unidad / Integración / Sistema |
| **Responsable código** | @usuario |
| **Responsable pruebas** | @usuario (≠ responsable código) |

---

## Historia de usuario

> **Como** [rol del usuario]
> **quiero** [acción o funcionalidad]
> **para** [valor o beneficio obtenido]

---

## Clases y métodos involucrados

| Clase | Método | Descripción |
|---|---|---|
|  |  |  |
|  |  |  |

---

## Prueba de aceptación

<!-- Criterio que determina que la historia es Done -->


---

## Diseño de pruebas (IEEE 829)

### Objetivo de la prueba
<!-- ¿Qué se persigue con esta prueba? -->

### Criterios de validez
<!-- Condiciones para que los resultados tengan sentido (ej: métodos colaboradores disponibles) -->

### Caja Negra — Clases de Equivalencia (CE)

| CP | Clase | Entrada | Resultado esperado | Válido/Inválido |
|---|---|---|---|---|
| CP1 |  |  |  |  |
| CP2 |  |  |  |  |

Ver Anexo CN (Caja Negra) en la documentación del proyecto.

### Caja Blanca — McCabe
<!-- Completar solo si la cobertura de decisión no se alcanza con Caja Negra -->
<!-- Complejidad ciclomática: -->
<!-- Caminos identificados: -->

| CP | Camino | Descripción |
|---|---|---|
|  |  |  |

Ver Anexo CB (Caja Blanca) en la documentación del proyecto.

---

## Criterio de paso / fallo

- **PASA**: el comportamiento observado coincide con el esperado; no se producen errores no contemplados.
- **FALLA**: el comportamiento difiere del esperado, no se respetan las reglas funcionales, o se producen excepciones inesperadas.

---

## 📌 Columna Kanban actual

- [ ] Preparada
- [ ] Codificación / DoneC
- [ ] DiseñoPruebas / DoneP
- [ ] CajaNegra / DoneCN
- [ ] CajaBlanca / DoneCB
- [ ] Integración / DoneI
- [ ] Informe
- [ ] Entregada

---

## Referencias

- Commit relacionado: <!-- enlace o hash -->
- Clase de prueba JUnit: `NombreTest.java`
- Tag GitHub: <!-- ej: sprint1-done -->
