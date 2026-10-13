import java.nio.file.Path

class VideojuegoService {
    val control: VideojuegoRepository = VideojuegoRepository()
    val ruta: Path = Path.of("datos/datos_ini", "videojuegos.csv")
    val videojuegos: MutableList<Videojuego> = control.leerDatosCsv(ruta) as MutableList<Videojuego>

    fun recibirDatos(): List<Videojuego> {
        return this.videojuegos
    }
    fun escribirDatos(videojuegos: MutableList<Videojuego>){
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
}