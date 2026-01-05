# ⌚ Práctica 9: Contador de Pasos para Wear OS

> Aplicación nativa para **Android Wear OS** desarrollada en **Kotlin** que implementa la lectura de sensores físicos para detectar movimiento.

## 📋 Descripción

Esta práctica está enfocada en el desarrollo de aplicaciones para dispositivos "vestibles" (Wearables). El objetivo principal es interactuar con el **Sensor Manager** de Android para leer datos en tiempo real.

Esta versión implementa un **algoritmo alternativo basado en el Acelerómetro**. Esto permite simular y detectar "pasos" calculando la magnitud de la fuerza G aplicada al dispositivo al agitarlo.

---
## 💻 Requisitos e Instalación

Para ejecutar este proyecto necesitas:
* **Android Studio** (versión Ladybug o superior recomendada).
* **Git** instalado en tu sistema.

### Pasos para instalar

1.  **Clonar el repositorio:**
    Abrir la terminal y ejecutar:
    ```bash
    git clone https://github.com/RUME05/Practica-9-Vestibles.git
    ```

2.  **Abrir en Android Studio:**
    * Iniciar Android Studio.
    * Seleccionar **File > Open**.
    * Buscar la carpeta del proyecto y selecciona "OK".
    * Espera a que Gradle termine de sincronizar las dependencias.

3.  **Configurar el Emulador (AVD):**
    Este proyecto está diseñado para relojes redondos.
    * Ir al **Device Manager**.
    * Crear un dispositivo **Wear OS Large Round**.
    * Imagen de Sistema recomendada: **API 30 (Android 11)**.

4.  **Ejecutar la App:**
    * Seleccionar el emulador creado y presiona el botón **Run**.
    * Si el sistema lo solicita, aceptar los permisos de **Actividad Física**.

---

### 🧪 Cómo probar en el Emulador

Dado que el emulador no puede "caminar" físicamente, la app utiliza el acelerómetro para detectar sacudidas manuales:

1.  Abrir la aplicación en el emulador.
2.  Haz clic en los tres puntos `...` de la barra lateral del emulador (**Extended Controls**).
3.  Ir a **Virtual Sensors** > **Accelerometer**.
4.  Mover los sliders de **Move** o **Rotate** (agitando el reloj virtual).
5.  Aumento del contador en la pantalla al superar el umbral de movimiento.

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje:** Kotlin
* **IDE:** Android Studio Ladybug (o superior)
* **SDK Target:** Android 11 (API 30 - Wear OS)
* **Componentes:**
    * `SensorManager`: Para gestión de hardware.
    * `SensorEventListener`: Para escuchar cambios en tiempo real.
    * `Activity`: Arquitectura ligera para Wear OS.

---

## ⚙️ Lógica del Sensor (Algoritmo)

Se utiliza el sensor `TYPE_ACCELEROMETER`. Se calcula la **magnitud del vector de aceleración** usando la fórmula física:

$$Magnitud = \sqrt{x^2 + y^2 + z^2}$$

* La gravedad estándar es **~9.8 m/s²**.
* El algoritmo detecta un "paso" cuando la magnitud supera el umbral de **12 m/s²**, lo cual indica una agitación intencional del reloj.

### Snippet de Código Principal

```kotlin
override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Cálculo de magnitud del movimiento (Teorema de Pitágoras 3D)
        val magnitud = Math.sqrt((x*x + y*y + z*z).toDouble())

        // Umbral de sensibilidad (>12 detecta sacudida)
        if (magnitud > 12) {
            pasosSimulados++
            actualizarUI(pasosSimulados)
        }
    }
}
```
---
**## 📸 Capturas de Pantalla

| Inicio de la App | Simulación en Emulador | Conteo de Pasos |
|:---:|:---:|:---:|
| ![Inicio](screenshots/inicio.png) | ![Sensores](screenshots/sensores.png) | ![Conteo](screenshots/conteo.png) |
| *Vista inicial en espera de movimiento.* | *Manipulación manual del acelerómetro en "Virtual Sensors".* | *Actualización de la UI al detectar movimientos bruscos.* |

---
