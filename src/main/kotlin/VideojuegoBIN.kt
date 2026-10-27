import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

data class VideojuegoBIN(
    val id: Int,
    val titulo: String,
    val genero: String,
    val anio: Int,
    var nota: Double
)

const val TAMANO_ID = Int.SIZE_BYTES
const val TAMANO_TITULO = 40
const val TAMANO_GENERO = 20
const val TAMANO_ANIO = Int.SIZE_BYTES
const val TAMANO_NOTA = Double.SIZE_BYTES
const val TAMANO_REGISTRO = TAMANO_ID + TAMANO_TITULO + TAMANO_GENERO + TAMANO_ANIO + TAMANO_NOTA

val archivoPath = Path.of("datos/datos_ini", "videojuegos.bin")


fun crearDesdeCSV() {
    val control = VideojuegoService()
    Files.createDirectories(archivoPath.parent)

    val videojuegos = csvToBIN(control.recibirDatos())
    vaciarCrearFichero()
    for(videojuego in videojuegos){
        anadirVideojuego(videojuego.id, videojuego.titulo, videojuego.genero, videojuego.anio, videojuego.nota)
    }
}
fun crearDesdeXML(){
    Files.createDirectories(archivoPath.parent)

    val videojuegos = xmlToBIN(recibirDatosXML())
    vaciarCrearFichero()
    for(videojuego in videojuegos){
        anadirVideojuego(videojuego.id, videojuego.titulo, videojuego.genero, videojuego.anio, videojuego.nota)
    }
}
fun crearDesdeJSON(){
    Files.createDirectories(archivoPath.parent)

    val videojuegos = jsonToBIN(recibirDatosJSON())
    vaciarCrearFichero()
    for(videojuego in videojuegos){
        anadirVideojuego(videojuego.id, videojuego.titulo, videojuego.genero, videojuego.anio, videojuego.nota)
    }
}


fun xmlToBIN(videojuegos: List<VideojuegoXML>): List<VideojuegoBIN> {
    return videojuegos.map{
        VideojuegoBIN(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun jsonToBIN(videojuegos:List<VideojuegoJSON>): List<VideojuegoBIN>{
    return videojuegos.map{
        VideojuegoBIN(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun csvToBIN(videojuegos: List<Videojuego>): List<VideojuegoBIN> {
    return videojuegos.map{
        VideojuegoBIN(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun vaciarCrearFichero() {
    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).close()
        println("--- El fichero '${archivoPath.fileName}' se ha creado y está vacío.")
    } catch (e: Exception) {
        println("Error al vaciar o crear el fichero: ${e.message}")
    }
}

fun anadirVideojuego(id: Int, titulo: String, genero: String, anio: Int, nota: Double) {
    val nuevoVideojuego = VideojuegoBIN(id, titulo, genero, anio, nota)

    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            buffer.putInt(nuevoVideojuego.id)
            val tituloBytes = nuevoVideojuego.titulo
                .padEnd(TAMANO_TITULO, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(tituloBytes, 0, TAMANO_TITULO)
            val generoBytes = nuevoVideojuego.genero
                .padEnd(TAMANO_GENERO, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(generoBytes, 0, TAMANO_GENERO)
            buffer.putInt(nuevoVideojuego.anio)
            buffer.putDouble(nuevoVideojuego.nota)

            buffer.flip()
            while (buffer.hasRemaining()) {
                canal.write(buffer)
            }
            println("- Videojuego '${nuevoVideojuego.titulo.trim()}' añadido correctamente.")
        }
    } catch (e: Exception) {
        println("Error al añadir el videojuego: ${e.message}")
    }
}

fun leerVideojuegos(): List<VideojuegoBIN> {
    val videojuegos = mutableListOf<VideojuegoBIN>()

    if (!Files.isReadable(archivoPath)) return emptyList()

    FileChannel.open(archivoPath, StandardOpenOption.READ).use { canal ->
        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
        while (canal.read(buffer) > 0) {
            buffer.flip()
            val id = buffer.getInt()
            val tituloBytes = ByteArray(TAMANO_TITULO)
            buffer.get(tituloBytes)
            val titulo = String(tituloBytes, Charsets.ISO_8859_1).trim()
            val generoBytes = ByteArray(TAMANO_GENERO)
            buffer.get(generoBytes)
            val genero = String(generoBytes, Charsets.ISO_8859_1).trim()
            val anio = buffer.getInt()
            val nota = buffer.getDouble()
            videojuegos.add(VideojuegoBIN(id, titulo, genero, anio, nota))
            buffer.clear()
        }
    }
    return videojuegos
}

fun obtenerUltimaIdBIN(): Int{
    var videojuegos = leerVideojuegos()
    return videojuegos.last().id + 1
}

fun mostrarInfo() {
    println("--- Videojuegos leídos secuencialmente del fichero .bin ---")
    val leidas = leerVideojuegos()
    if(!leidas.isEmpty()) {
        for (i in leidas) {
            println(" - ID: ${i.id} | Título: ${i.titulo} | Género: ${i.genero} | Año: ${i.anio} | Nota: ${i.nota}")
        }
    }else{
        println("--- LISTA VACÍA ---")
    }
}