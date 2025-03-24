package bruno.kmp.domain.mapper

import bruno.kmp.data.remote.ApiCharacter
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Gender
import bruno.kmp.domain.model.Status

class ApiCharacterMapper : Mapper<ApiCharacter, Character>() {
    override fun map(model: ApiCharacter): Character = model.run {
        Character(
            id, name, when (status) {
                "Alive" -> Status.ALIVE
                "Dead" -> Status.DEAD
                else -> Status.UNKNOWN
            }, species, when (gender) {
                "Male" -> Gender.MALE
                "Female" -> Gender.FEMALE
                "Genderless" -> Gender.GENDERLESS
                else -> Gender.UNKNOWN
            }, type, origin.name, location.name, image, ""
        )
    }

    override fun inverseMap(model: Character): ApiCharacter {
        TODO("Not yet implemented")
    }
}