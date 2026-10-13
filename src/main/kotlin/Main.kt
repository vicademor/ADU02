import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.system.exitProcess


fun main(){
    val datos: Path = Path.of("datos")
    val datosIni: Path = Path.of("$datos/datos_ini")
    val datosFin: Path = Path.of("$datos/datos_fin")
    val fichero: Path = Path.of("$datosIni/videojuegos.csv")

    println("CREACIÓN DE RUTAS PROYECTO\nCreando rutas...")

    if(!datosIni.exists()) {
        println("Creación de ruta para DATOS_INI")
        Files.createDirectories(datosIni)
    }
    if(!datosFin.exists()) {
        println("Creación de ruta para DATOS_FIN")
        Files.createDirectories(datosFin)
    }
    if(!fichero.exists()) {
        mostrarRutas(datos)
        Files.createFile(fichero)
    }else {
        mostrarRutas(datos)
    }
    menuPrincipal()
}
fun mostrarRutas(datos: Path){
    println("MOSTRANDO ESTRUCTURA DE DIRECTORIOS Y FICHEROS")
    Files.walk(datos).use { stream ->
        stream.sorted().forEach { path ->
            val profundidad = path.nameCount - datos.nameCount
            val indentacion = "\t".repeat(profundidad)
            val prefijo = if (Files.isDirectory(path)) "[DIR]" else "[FILE]"
            if(profundidad > 0){
                println("$indentacion$prefijo ${path.fileName}")
            }else{
                println("$prefijo ${path.fileName}")
            }
        }

        println()
    }
}
fun menuPrincipal(){
    var opcion: Int = 0
    while(opcion != 1) {
        println(
            """
        ===== CATÁLOGO DE VIDEOJUEGOS =====
        1. Gestión CSV
        0. Salir
        ====================================
        Elige una opción:
    """.trimIndent()
        )
        opcion = readln().toInt()
        when (opcion) {
            1 -> menuCsv()
            2 -> exitProcess(0)
            else -> println("Opción incorrecta")
        }
    }
}
fun menuCsv(){
    VideojuegoService()
    val control: VideojuegoService = VideojuegoService()
    var opcion: Int? = null
    while(opcion != 0) {
        println(
            """
        --- Gestión CSV ---
        1. Listar todos los videojuegos
        2. Buscar videojuego por ID
        3. Añadir nuevo videojuego
        4. Modificar nota de un videojuego
        5. Eliminar videojuego
        0. Volver al menú principal
    """.trimIndent())
        opcion = readln().toInt()
        when (opcion) {
            1 -> control.mostrarLista()
            2 -> buscarId(control)
            3 -> control.anyadirJuego(crearVideojuego(control))
            4 -> modificarVideojuego(control)
            5 -> eliminarVideojuego(control)
            0 -> menuPrincipal()
            else -> println("Entrada inválida")
        }
    }
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

fun buscarId(control: VideojuegoService){
    println("--- Buscar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    control.buscarPorId(id)
}

fun modificarVideojuego(control: VideojuegoService){
    println("--- Modificar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    println("Introduce la nueva nota: ")
    val nota: Double = readln().toDouble()
    control.modificarNota(id, nota)
}

fun eliminarVideojuego(control: VideojuegoService){
    println("--- Eliminar videojuego ---")
    println("Introduce su id: ")
    val id: Int = readln().toInt()
    if(!control.eliminarJuego(id)) println("--- Videojuego no encontrado ---")
}