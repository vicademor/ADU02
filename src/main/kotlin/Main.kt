import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.system.exitProcess

fun main() {
    val datos: Path = Path.of("datos")
    val datosIni: Path = Path.of("$datos/datos_ini")
    val datosFin: Path = Path.of("$datos/datos_fin")
    val fichero: Path = Path.of("$datosIni/videojuegos.csv")

    println("CREACIÓN DE RUTAS PROYECTO\nCreando rutas...")

    if (!datosIni.exists()) {
        println("Creación de ruta para DATOS_INI")
        Files.createDirectories(datosIni)
    }
    if (!datosFin.exists()) {
        println("Creación de ruta para DATOS_FIN")
        Files.createDirectories(datosFin)
    }
    if (!fichero.exists()) {
        mostrarRutas(datos)
        Files.createFile(fichero)
    } else {
        mostrarRutas(datos)
    }
    menuPrincipal()
}

fun mostrarRutas(datos: Path) {
    println("MOSTRANDO ESTRUCTURA DE DIRECTORIOS Y FICHEROS")
    Files.walk(datos).use { stream ->
        stream.sorted().forEach { path ->
            val profundidad = path.nameCount - datos.nameCount
            val indentacion = "\t".repeat(profundidad)
            val prefijo = if (Files.isDirectory(path)) "[DIR]" else "[FILE]"
            if (profundidad > 0) {
                println("$indentacion$prefijo ${path.fileName}")
            } else {
                println("$prefijo ${path.fileName}")
            }
        }

        println()
    }
}

fun menuPrincipal() {
    var opcion: Int = 0
    while (true) {
        println(
            """
        ===== CATÁLOGO DE VIDEOJUEGOS =====
        1. Gestión CSV
        2. Leer catálogo desde XML
        3. Leer catálogo desde JSON
        4. Conversiones entre formatos
        5. Gestión fichero BIN
        0. Salir
        ====================================
        Elige una opción:
    """.trimIndent()
        )
        opcion = readln().toInt()
        when (opcion) {
            1 -> menuCsv()
            2 -> menuXML()
            3 -> menuJSON()
            4 -> menuConversion()
            5 -> menuBIN()
            0 -> exitProcess(0)
            else -> println("Opción incorrecta")
        }
    }
}

fun menuCsv() {
    VideojuegoService()
    val control: VideojuegoService = VideojuegoService()
    var opcion: Int? = null
    while (opcion != 0) {
        println(
            """
        --- Gestión CSV ---
        1. Listar todos los videojuegos
        2. Buscar videojuego por ID
        3. Añadir nuevo videojuego
        4. Modificar nota de un videojuego
        5. Eliminar videojuego
        0. Volver al menú principal
    """.trimIndent()
        )
        opcion = readln().toInt()
        when (opcion) {
            1 -> control.mostrarLista()
            2 -> buscarId(control)
            3 -> control.anyadirJuego(crearVideojuego(control))
            4 -> modificarVideojuego(control)
            5 -> eliminarVideojuego(control)
            0 -> volverMenuPrincipal(control)
            else -> println("Entrada inválida")
        }
    }
}

fun menuConversion() {
    val control = VideojuegoService()
    println(
        """
        --- Conversiones ---
        1. CSV  → JSON
        2. CSV  → XML
        3. JSON → CSV
        4. JSON → XML
        5. XML  → CSV
        6. XML  → JSON
        0. Volver
    """.trimIndent()
    )
    var opcion = readln().toInt()
    when (opcion) {
        1 -> control.escribirDatosJSONFin(csvToJson(control.recibirDatos()))
        2 -> control.escribirDatosXMLFin(csvToXML(control.recibirDatos()))
        3 -> control.escribirDatosCSVFin(jsonToCsv(recibirDatosJSON()) as MutableList<Videojuego>)
        4 -> control.escribirDatosXMLFin(control.jsonToXML(recibirDatosJSON()))
        5 -> control.escribirDatosCSVFin(xmlToCsv(recibirDatosXML()) as MutableList<Videojuego>)
        6 -> control.escribirDatosJSONFin(control.xmlToJSON(recibirDatosXML()))
        0 -> menuPrincipal()
    }
}

fun menuXML() {
    gestionXML()
}

fun menuJSON() {
    gestionJSON()
}

fun menuBIN() {
    vaciarCrearFichero()
    var opcion = 1
    while (opcion != 0) {
        println(
            """
        ===== CRUD fichero BIN =====
        1. Importar información de un fichero de intercambio.
        2. Leer información del fichero binario.
        3. Añadir un registro nuevo
        4. Modificar un registro existente (por ID)
        5. Eliminar un registro existente (por ID)
        0. Volver
    """.trimIndent()
        )
        opcion = readln().toInt()
        when (opcion) {
            1 -> importarInfoBIN()
            2 -> mostrarInfo()
            3 -> anyadirVideojuegoBIN()
            4 -> modificarVideojuegoBIN()
            5 -> eliminarJuegoBIN()
            0 -> menuPrincipal()
            else -> println("Entrada inválida")
        }
    }
}

fun importarInfoBIN() {
    println(
        """
        === IMPORTAR DATOS BIN ===
        1. Desde CSV
        2. Desde XML
        3. Desde JSON
        0. Volver
    """.trimIndent()
    )
    var opcion = readln().toInt()
    when (opcion) {
        1 -> crearDesdeCSV()
        2 -> crearDesdeXML()
        3 -> crearDesdeJSON()
        0 -> menuPrincipal()
        else -> println("Entrada inválida")
    }
}

fun anyadirVideojuegoBIN() {
    println("Introduce título: ")
    var titulo = readln()
    println("Introduce género: ")
    var genero = readln()
    println("Introduce año: ")
    var anio = readln().toInt()
    println("Introduce nota: ")
    var nota = readln().toDouble()
    anadirVideojuego(obtenerUltimaIdBIN(), titulo, genero, anio, nota)
}

fun modificarVideojuegoBIN() {
    var valido: Boolean = false
    var index: Int = 0
    var datos = leerVideojuegos()
    println("Introduce ID a modificar: ")
    while (!valido) {
        try {
            valido = true
            var id = readln().toInt()
            for (i in datos) {
                index++
                if (id == i.id) {
                    println("--- Videojuego encontrado: ${i.titulo} ---")
                    break
                }
            }
            println("Nueva nota: ")
            var nota = readln().toDouble()
            datos.get(index-1).nota = nota
            vaciarCrearFichero()
            for (j in datos) {
                anadirVideojuego(j.id, j.titulo, j.genero, j.anio, j.nota)
            }
            println("=== INFORMACIÓN ACTUALIZADA ===")
        } catch (e: Exception) {
            println("Entrada no válida: " + e.message)
            valido = false
        }

    }
}

fun eliminarJuegoBIN() {
    var valido: Boolean = false
    var index: Int = 0
    var datos = leerVideojuegos() as MutableList<VideojuegoBIN>
    println("Introduce ID a eliminar: ")
    while (!valido) {
        try {
            valido = true
            var id = readln().toInt()
            for (i in datos) {
                index++
                if (id == i.id) {
                    println("--- Videojuego encontrado: ${i.titulo} ---")
                    break
                }
            }
            println(
                """
            ¿CONFIRMAS BORRADO?
            1. Sí
            2. No
        """.trimMargin()
            )
            var confirmacion = readln().toInt()
            when (confirmacion) {
                1 -> {
                    datos.removeAt(index-1)
                    println("=== VIDEOJUEGO ELIMINADO ====")
                    vaciarCrearFichero()
                    for (j in datos) {
                        anadirVideojuego(j.id, j.titulo, j.genero, j.anio, j.nota)
                    }
                }

                2 -> println("=== ELIMINACIÓN CANCELADA ===")
                else -> println("Entrada inválida")
            }
        } catch (e: Exception) {
            println("Entrada no válida: " + e.message)
            valido = false
        }

    }
}

fun volverMenuPrincipal(control: VideojuegoService) {
    control.actualizarXML()
    control.actualizarJSON()
    menuPrincipal()
}

fun crearVideojuego(control: VideojuegoService): Videojuego {
    println("--- Creando videojuego ---")
    println("Introduce un título: ")
    val titulo: String = readln()
    println("Introduce un género: ")
    val genero: String = readln()
    println("Introduce año de salida: ")
    val anio: Int = readln().toInt()
    println("Introduce la nota: ")
    val nota: Double = readln().toDouble()
    val id: Int = control.obtenerUltimaId()
    return Videojuego(id, titulo, genero, anio, nota)
}

fun buscarId(control: VideojuegoService) {
    println("--- Buscar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    control.buscarPorId(id)
}

fun modificarVideojuego(control: VideojuegoService) {
    println("--- Modificar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    println("Introduce la nueva nota: ")
    val nota: Double = readln().toDouble()
    control.modificarNota(id, nota)
}

fun eliminarVideojuego(control: VideojuegoService) {
    println("--- Eliminar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    if (!control.eliminarJuego(id)) println("--- Videojuego no encontrado ---")
}