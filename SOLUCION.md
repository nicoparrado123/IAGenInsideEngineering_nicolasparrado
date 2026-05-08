# SOLUCION.md

---

## Problema #1: El Videoclub de Don Mario

### Prompt utilizado

> "Tengo que hacer un sistema de alquiler de películas en Java por consola. Hay películas físicas y digitales, cada una con título, precio y disponibilidad. El cliente tiene membresía básica (precio normal) o premium (20% de descuento). Al final se muestra un recibo con las películas, subtotal, descuento y total. ¿Qué patrones de diseño usarías y cómo lo implementarías?"

---

### Patrones de diseño utilizados

**Strategy** para las membresías: en vez de poner un `if` gigante que diga "si es premium descuenta, si no no", se crea una interfaz `Membresia` con el método `calcularTotal`. Cada tipo de membresía implementa su propia lógica. Si mañana aparece una membresía VIP con 40% de descuento, solo se agrega una clase nueva sin tocar nada más.

**Polimorfismo con herencia** para las películas: `Pelicula` es la clase base abstracta, y `PeliculaFisica` y `PeliculaDigital` la extienden. Cada una sabe su propio tipo. Esto permite tratar todas las películas igual en el catálogo sin importar si son físicas o digitales.

---

### Principios SOLID aplicados

- **S (Single Responsibility)**: cada clase tiene una sola razón para cambiar. `Pelicula` maneja datos de película, `Membresia` maneja el descuento, `Videoclub` maneja el flujo del programa.
- **O (Open/Closed)**: para agregar un nuevo tipo de membresía o película, se crea una clase nueva sin modificar las existentes.
- **L (Liskov Substitution)**: `PeliculaFisica` y `PeliculaDigital` pueden usarse donde se espera una `Pelicula` sin romper nada.
- **D (Dependency Inversion)**: `Videoclub` depende de la interfaz `Membresia`, no de `MembresiaBasica` o `MembresiaPremium` directamente.

---

### Estructura de archivos creados

```
videoclub/
├── Pelicula.java          ← clase abstracta base
├── PeliculaFisica.java    ← extiende Pelicula
├── PeliculaDigital.java   ← extiende Pelicula
├── Membresia.java         ← interfaz Strategy
├── MembresiaBasica.java   ← sin descuento
├── MembresiaPremium.java  ← 20% de descuento
└── Videoclub.java         ← main con el flujo completo
```

---

### Evidencia de ejecución

```
=== Videoclub de Don Mario ===
Tipo de membresia (basica/premium):
premium

Peliculas disponibles:
1. [Fisica] Interestellar - $8000 - Disponible
2. [Fisica] El Padrino - $7000 - No disponible
3. [Digital] Inception - $5000 - Disponible
4. [Digital] Matrix - $6000 - Disponible

Seleccione peliculas (numeros separados por coma):
1,3

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8000
 - Inception (Digital) - $5000
Subtotal: $13000
Descuento (20%): $2600
Total a pagar: $10400
--------------------------
¡Disfrute su pelicula!
```

---

---

## Problema #2: Tienda Virtual

### Prompt utilizado

> "Tengo un sistema de pagos en Java con clases CreditCardFactory, PaypalFactory, CryptoFactory que extienden PaymentMethod, y una clase ECIPayment que usa una interfaz PaymentFactory que no existe todavía. También hay un PaymentEventObserver que implementa PaymentObserver y usa Inventory, Facturation y Notification. El código no compila. Ayúdame a identificar qué falta, qué errores hay y cómo corregirlos."

---

### 1. ¿Qué dos patrones de diseño se están usando?

**Abstract Factory**: las clases `CreditCardFactory`, `PaypalFactory` y `CryptoFactory` son fábricas concretas que crean objetos `PaymentMethod`. La interfaz `PaymentFactory` define el contrato para crear métodos de pago sin que `ECIPayment` sepa cuál es el concreto.

**Observer**: `ECIPayment` es el sujeto (subject) que notifica a sus observadores cuando un pago tiene éxito o falla. `PaymentEventObserver` implementa `PaymentObserver` y reacciona coordinando inventario, facturación y notificaciones.

¿Son los adecuados? Sí. Abstract Factory es perfecto para crear familias de objetos relacionados (pago + validación) sin acoplar el código cliente. Observer es ideal para notificar módulos independientes sin que el core de pagos los conozca directamente.

---

### 2. Clases/interfaces que faltaban

La pieza que hacía que todo no compilara era la interfaz `PaymentFactory`. `ECIPayment` la usaba como tipo en su método `processPayment` pero no existía en ningún lado. Se creó:

```java
public interface PaymentFactory {
    PaymentMethod createPaymentMethod(double amount, String customerId, String description);
}
```

Con esto, `CreditCardFactory`, `PaypalFactory` y `CryptoFactory` deben implementar esta interfaz para poder pasarse como argumento a `ECIPayment`.

---

### 3. ¿El diagrama de contexto es suficiente?

El diagrama muestra la relación general entre los módulos (pagos, inventario, facturación, notificaciones) y es útil para entender el flujo. Sin embargo, le falta mostrar:

- La interfaz `PaymentFactory` y su relación con las fábricas concretas
- La interfaz `ValidatePayment` que implementa `PaymentMethod`
- La relación entre `PaymentMethod` y `PaymentStatus`

Para el diagrama de clases UML sería más completo incluir estas interfaces y sus implementaciones.

---

### 4. Errores encontrados en el código

**Error 1 — `PaymentFactory` no existía** (el más grave, causa de que no compilara)
`ECIPayment.processPayment()` recibe un parámetro de tipo `PaymentFactory` pero esa interfaz no estaba definida en ningún archivo. Solución: crear `PaymentFactory.java`.

**Error 2 — Import incorrecto en `PaymentEventObserver`**
```java
// MAL: importa la clase de javax.management, no la del proyecto
import javax.management.Notification;
private Notification notification;
```
La clase `Notification` del proyecto está en el mismo paquete, no hay que importarla de `javax.management`. Solución: eliminar el import y usar el nombre completamente calificado o simplemente quitar el import incorrecto.

**Error 3 — Bug en el constructor de `PaymentMethod`**
```java
// MAL: el parámetro se llama transactionID pero se asigna a customerID
public PaymentMethod(double amount, String transactionID, String description) {
    this.customerID = customerID; // ← customerID es null aquí, no usa el parámetro
```
El parámetro debería llamarse `customerID` para que la asignación tenga sentido. Todos los clientes quedaban con `customerID = null`.

**Error 4 — Bug en `CryptoFactory`**
```java
// MAL: token no viene como parámetro del constructor
this.token = token; // ← token es null, no está en los parámetros
```
El constructor no recibe `token` como argumento, así que `this.token = token` asigna `null`. Se eliminó esa línea ya que `token` no se usa en ninguna validación.

---

### 5. Correcciones aplicadas

| Archivo | Cambio |
|---|---|
| `PaymentFactory.java` | Creado desde cero — interfaz que faltaba |
| `PaymentEventObserver.java` | Eliminado `import javax.management.Notification` |
| `PaymentMethod.java` | Parámetro renombrado de `transactionID` a `customerID` |
| `CryptoFactory.java` | Eliminada la línea `this.token = token` |

---

### 6. Resultado de las pruebas

```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Todas las pruebas pasan correctamente después de las correcciones.

---
