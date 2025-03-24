package bruno.kmp.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val type: String,
    val origin: String,
    val location: String,
    val image: String,
    var page: String
)