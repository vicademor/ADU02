import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Path
import kotlin.collections.emptyList

data class VideojuegoXML (
    @JacksonXmlProperty (localName = "id")
    val id: Int,
    @JacksonXmlProperty (localName = "titulo")
    val titulo: String,
    @JacksonXmlProperty (localName = "genero")
    val genero: String,
    @JacksonXmlProperty (localName = "anio")
    val anio: Int,
    @JacksonXmlProperty (localName = "nota")
    val nota: Double
)

@JacksonXmlRootElement(localName = "videojuegos")
data class VideojuegosWrapper (
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "videojuego")
    val videojuegos: List<VideojuegoXML> = emptyList()
)

fun main(){
    gestionXML()
}

fun gestionXML(){
    val entradaXML = Path.of("datos/datos_ini", "videojuegos.xml")
    val salidaXML = Path.of("datos/datos_ini", "videojuegos.xml")
    val control = VideojuegoService()
    val datos = leerDatosXML(entradaXML)
    println("--- Información de la lista de objetos Videojuegos XML ---")
    for (videojuego in datos){
        println(" - ID: ${videojuego.id} | Título: ${videojuego.titulo} | Genero: ${videojuego.genero} | Año de salida: ${videojuego.anio} | Nota: ${videojuego.nota}")
    }

    escribirDatosXML(salidaXML, datos)
    val datoscsv: MutableList<Videojuego> = control.recibirDatos() as MutableList<Videojuego>
    val datosxml = xmlToCsv(datos)
    var contador = 0
    datosxml.forEach {
        if (datoscsv.none { juego -> juego.id == it.id }) {
            contador++
            datoscsv.add(it)
        }
    }
    control.escribirDatos(datoscsv)
    println("--- $contador juegos añadidos ---")
}

fun recibirDatosXML(): List<VideojuegoXML> {
    val entradaXML = Path.of("datos/datos_ini", "videojuegos.xml")
    val datos = leerDatosXML(entradaXML)
    return datos
}

fun xmlToCsv(videojuegosXML: List<VideojuegoXML>): List<Videojuego> {
    return videojuegosXML.map {
        Videojuego(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun csvToXML(videojuegos: List<Videojuego>): List<VideojuegoXML>{
    return videojuegos.map{
        VideojuegoXML(
            id = it.id,
            titulo = it.titulo,
            genero = it.genero,
            anio = it.anio,
            nota = it.nota
        )
    }
}

fun leerDatosXML(ruta: Path) : List<VideojuegoXML>{
    var contenedor = VideojuegosWrapper(emptyList())

    if(!Files.isReadable(ruta)){
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    }else{
        val fichero = ruta.toFile()
        val xmlMapper = XmlMapper().registerKotlinModule()

        contenedor = xmlMapper.readValue(fichero)
        println("--- Información leída con éxito de: $ruta ---")
    }
    return contenedor.videojuegos
}

fun escribirDatosXML(ruta: Path, videojuegos: List<VideojuegoXML>){
    try {
        val fichero = ruta.toFile()
        val contenedor = VideojuegosWrapper(videojuegos)
        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedor)
        fichero.writeText(xmlString)

        println("--- Información guardada en XML: $fichero")
    } catch (e: Exception) {
        println("Error al guardar XML: ${e.message}")
    }
}