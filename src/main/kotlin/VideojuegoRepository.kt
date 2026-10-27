import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class VideojuegoRepository {
    fun leerDatosCsv(ruta: Path): List<Videojuego> {
        var videojuegos: List<Videojuego> = emptyList()

        if (!Files.exists(ruta)) {
            println("Error: No se puede leer el fichero en la ruta: $ruta")
        } else {
            val reader = csvReader {
                delimiter = ';'
            }
            val filas: List<List<String>> = reader.readAll(ruta.toFile())

            videojuegos = filas.mapNotNull { columnas ->
                if (columnas.size >= 5) {
                    try {
                        val id = columnas[0].toInt()
                        val titulo = columnas[1]
                        val genero = columnas[2]
                        val anio = columnas[3].toInt()
                        val nota = columnas[4].toDouble()
                        Videojuego(id, titulo, genero, anio, nota)
                    } catch (e: Exception) {
                        println("Fila inválida ignorada: $columnas -> Error ${e.message}")
                        null
                    }
                } else {
                    println("Fila con formato incompleto ignorada: $columnas")
                    null
                }
            }
        }
        println("--- Información leída con éxito de: $ruta")
        return videojuegos
    }

    fun escribirCSV(ruta: Path, videojuegos: MutableList<Videojuego>) {
        try {
            if (!Files.isReadable(ruta)) {
                Files.createFile(ruta)
                println("Videojuegos.csv creado por primera vez en: $ruta")
            }
            val fichero: File = ruta.toFile()
            csvWriter {
                delimiter = ';'
            }.writeAll(
                videojuegos.map { videojuego ->
                    listOf(
                        videojuego.id.toString(),
                        videojuego.titulo,
                        videojuego.genero,
                        videojuego.anio.toString(),
                        videojuego.nota.toString()
                    )
                },
                fichero
            )
            println("--- Información guardada con éxito en: $fichero")

        } catch (e: Exception) {
            println("Error al escribir el fichero CSV: ${e.message}")
        }
    }
}

