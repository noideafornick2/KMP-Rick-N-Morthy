package bruno.kmp.presentation.core.composables.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruno.kmp.domain.model.Character
import coil3.compose.AsyncImage

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 10.dp, start = 10.dp, bottom = 10.dp)
                .width(110.dp)
                .height(110.dp)
        )
        Text(
            text = character.name + " (" +character.status+")",
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

