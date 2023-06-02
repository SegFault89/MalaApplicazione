package it.dario.malaapplicazione.data.model

/**
 * Classe per ricordare quali sono i fogli di disponibilità presenti nel file
 *
 * @param fogli lista dei nomi dei fogli di disponibilità
 */
data class MalaFile(
    val fogli: List<String>
) {

    /**
     * lista di foglio nel file
     */
    private val _malaFogli: MutableMap<String, Foglio> = mutableMapOf()
    val malaFogli: Map<String, Foglio> get() = _malaFogli

    /**
     * aggiunge un foglio al MalaFile, lanciando un errore se già ne esiste uno con quel nome
     * @param foglio il foglio da inserire
     */
    fun addFoglio(foglio: Foglio) {
        if (_malaFogli.containsKey(foglio.label)) {
            error("${foglio.label} già presente")
        } else {
            _malaFogli[foglio.label] = foglio
        }
    }

}