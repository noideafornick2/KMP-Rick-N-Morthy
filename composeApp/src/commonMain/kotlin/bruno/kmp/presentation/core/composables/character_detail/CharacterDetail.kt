package bruno.kmp.presentation.core.composables.character_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruno.kmp.domain.model.Character
import bruno.kmp.domain.model.Status
import coil3.compose.AsyncImage

@Composable
fun CharacterDetail(character: Character) {
    val status = character.status
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.size(10.dp))
        AsyncImage(
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp),
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Origin: ${character.origin}",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Specie: ${character.species}",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Gender: ${character.gender}",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Type: ${character.type}",
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Status: "+status.name, color = when (status) {
                Status.ALIVE -> Color.Green
                Status.DEAD -> Color.Red
                Status.UNKNOWN -> Color.Gray
            },
            style = MaterialTheme.typography.h6
        )
    }
}