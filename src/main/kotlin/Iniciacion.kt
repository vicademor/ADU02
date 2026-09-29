import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.name


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