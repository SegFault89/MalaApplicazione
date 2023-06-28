package it.dario.malaapplicazione.data.model

data class LinkSection (
    val order: Int = 0,
    val sezione: String = "",
    val elementi: List<Link> = listOf()
 )
