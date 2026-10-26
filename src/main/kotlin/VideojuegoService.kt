import java.nio.file.Path

class VideojuegoService {
    val control: VideojuegoRepository = VideojuegoRepository()
    val ruta: Path = Path.of("datos/datos_ini", "videojuegos.csv")
    val videojuegos: MutableList<Videojuego> = control.leerDatosCsv(ruta) as MutableList<Videojuego>

    fun recibirDatos(): List<Videojuego> {
        return this.videojuegos
    }
    fun escribirDatos(videojuegos: MutableList<Videojuego>){
        videojuegos.sortBy { it.id }
        control.escribirCSV(ruta, videojuegos)
    }

    fun mostrarLista(){
        videojuegos.forEach {
            println(it.toString())
        }
    }

    fun buscarPorId(id: Int){
        var check: Boolean = false
        videojuegos.forEach {
            if(it.id == id){
                println("--- Videojuego encontrado ---")
                println(it.toString())
                check = true
            }
        }
        if(!check) {
            println("--- Videojuego no encontrado ---")
        }
    }

    fun anyadirJuego(videojuego: Videojuego){
        if(videojuegos.add(videojuego)) println("--- Videojuego añadido con éxito ---") else println("--- Error añadiendo el videojuego ---")
        escribirDatos(videojuegos)
    }

    fun modificarNota(id: Int, nota: Double){
        var check: Boolean = false
        if(nota !in 0.0..10.0){
            println("--- Nota inválida ---")
        }else {
            videojuegos.forEach {
                if (it.id == id) {
                    println("--- Videojuego modificado ---")
                    it.nota = nota
                    check = true
                }
            }
            if (!check) {
                println("--- Videojuego no modificado ---")
            } else {
                escribirDatos(videojuegos)
            }
        }
    }

    fun eliminarJuego(id: Int): Boolean{
        videojuegos.forEach {
            if(it.id == id){
                println("--- Videojuego eliminado---")
                videojuegos.remove(it)
                return true
            }
        }
        return false
    }

    fun obtenerUltimaId(): Int{
        return videojuegos.last().id + 1
    }

    fun actualizarXML(){
        var datos = csvToXML(this.videojuegos)
        var ruta = Path.of("datos/datos_ini", "videojuegos.xml")
        escribirDatosXML(ruta, datos)
    }

    fun actualizarJSON(){
        var datos = csvToJson(this.videojuegos)
        var ruta = Path.of("datos/datos_ini", "videojuegos.json")
        escribirDatosJSON(ruta, datos)
    }


    fun xmlToJSON(datos: List<VideojuegoXML>): List<VideojuegoJSON> {
        return datos.map{
            VideojuegoJSON(
                id = it.id,
                titulo = it.titulo,
                genero = it.genero,
                anio = it.anio,
                nota = it.nota
            )
        }
    }

    fun jsonToXML(datos: List<VideojuegoJSON>): List<VideojuegoXML> {
        return datos.map{
            VideojuegoXML(
                id = it.id,
                titulo = it.titulo,
                genero = it.genero,
                anio = it.anio,
                nota = it.nota
            )
        }
    }

    fun escribirDatosCSVFin(datos: MutableList<Videojuego>){
        val ruta = Path.of("datos/datos_fin", "videojuegos.csv")
        VideojuegoRepository().escribirCSV(ruta, datos)
    }

    fun escribirDatosXMLFin(datos: List<VideojuegoXML>){
        val ruta = Path.of("datos/datos_fin", "videojuegos.xml")
        escribirDatosXML(ruta, datos)
    }

    fun escribirDatosJSONFin(datos: List<VideojuegoJSON>){
        val ruta = Path.of("datos/datos_fin", "videojuegos.json")
        escribirDatosJSON(ruta, datos)
    }
}