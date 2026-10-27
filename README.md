# Catálogo de Videojuegos - Acceso a Datos (UD02)

Programa de consola en Kotlin para gestionar un catálogo de videojuegos.
Permite leer y realizar operaciones CRUD sobre datos en CSV, convertirlos a XML y JSON, y persistirlos en un fichero binario de acceso aleatorio.

## 1. Estructura de datos

### **Data Class:**

```kotlin
data class Videojuego(
    val id: Int,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double
)
```

### **Estructura del registro binario:**

- **ID**: Int - 4 bytes
- **titulo**: String - 40 bytes (longitud fija)
- **genero**: String - 20 bytes (longitud fija)
- **anio**: Int - 4 bytes
- **nota**: Double - 8 bytes
- **Tamaño Total del Registro**: 4 + 40 + 20 + 4 + 8 = 76 bytes

## 2. Instrucciones de ejecución

- **Requisitos previos**: JDK 17 o superior.
- **Compilación**: Abre el proyecto en IntelliJ IDEA o entorno compatible con Gradle para sincronizar las dependencias (`kotlinx-serialization-json`, `jackson-dataformat-xml`).
- **Estructura de directorios**: La ejecución genera automáticamente las carpetas `datos/datos_ini/` (persistencia principal) y `datos/datos_fin/` (exportaciones y copias de prueba).
- **Ejecución principal**: Inicia `Main.kt` para acceder a los menús interactivos de gestión CSV, lectura XML/JSON, acceso a fichero binario y el menú dedicado de conversión de formatos.

## 3. Decisiones de diseño

- **Gestión de ficheros binarios**: Se implementó la API Java NIO (`FileChannel` y `ByteBuffer`) para manipular el almacenamiento en registros de tamaño exacto (76 bytes), permitiendo un acceso estructurado.
- **Límites de texto**: Se reservaron 40 bytes para el título y 20 bytes para el género para optimizar el peso del registro manteniendo margen suficiente para los datos reales.
- **Conversión integral**: Se centralizó la transformación bidireccional entre CSV, XML y JSON en un menú específico para garantizar la integridad estructural de los datos independientemente del origen.
- **Autor**: Víctor Adelantado Moreno.