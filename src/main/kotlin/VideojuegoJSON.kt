import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.SystemColor.control
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class VideojuegoJSON(
    @SerialName("id") val id: Int,
    @SerialName("titulo") val titulo: String,
    @SerialName("genero") val genero: String,
    @SerialName("anio") val anio: Int,
    @SerialName("nota") val nota: Double
)

fun main() {

}

fun gestionJSON() {
    val control = VideojuegoService()
    val entradaJSON = Path.of("datos/datos_ini", "videojuegos.json")
    val salidaJSON = Path.of("datos/datos_ini", "videojuegos.json")
    val datos = leerDatosJSON(entradaJSON)
    println("--- Información de la lista de objetos Videojuegos JSON ---")
    for (videojuego in datos){
        println(" - ID: ${videojuego.id} | Título: ${videojuego.titulo} | Genero: ${videojuego.genero} | Año de salida: ${videojuego.anio} | Nota: ${videojuego.nota}")
    }
    escribirDatosJSON(salidaJSON, datos)
    val datoscsv: MutableList<Videojuego> = control.recibirDatos() as MutableList<Videojuego>
    val datosjson = jsonToCsv(datos)
    var contador = 0
    datosjson.forEach{
        if(datoscsv.none { juego -> juego.id == it.id }){
            contador++
            datoscsv.add(it)
        }
    }
    control.escribirDatos(datoscsv)
    println("--- $contador juegos añadidos ---")
}

fun recibirDatosJSON(): List<VideojuegoJSON> {
    val entradaJSON = Path.of("datos/datos_ini", "videojuegos.json")
    val datos = leerDatosJSON(entradaJSON)
    return datos
}

fun jsonToCsv(videojuegos: List<VideojuegoJSON>): List<Videojuego> {
    return videojuegos.map {
        Videojuego(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun csvToJson(videojuegos: List<Videojuego>): List<VideojuegoJSON> {
    return videojuegos.map {
        VideojuegoJSON(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun leerDatosJSON(ruta: Path): List<VideojuegoJSON> {
    var videojuegos: List<VideojuegoJSON> = emptyList()
    val jsonString = Files.readString(ruta)
    videojuegos = Json.decodeFromString<List<VideojuegoJSON>>(jsonString)
    println("--- Información leída con éxito de: $ruta ---")
    return videojuegos
}

fun escribirDatosJSON(ruta: Path, videojuegos: List<VideojuegoJSON>) {
    try {
        val jsonConfigurador = Json { prettyPrint = true }
        val jsonString = jsonConfigurador.encodeToString(videojuegos)
        Files.writeString(ruta, jsonString)
        println("--- Información guardada en: $ruta")
    } catch (e: Throwable) {
        println("Error al guardar JSON: ${e.message}")
    }
}
