package bruno.kmp.presentation.core.composables.character_detail

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import bruno.kmp.domain.model.Character
import bruno.kmp.presentation.core.composables.common.ArrowBackIcon
import bruno.kmp.presentation.core.composables.common.ManagementResourceUiState
import bruno.kmp.presentation.model.ResourceUiState

@Composable
fun CharacterDetailActionBar(
    character: ResourceUiState<Character>,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        title = {
            ManagementResourceUiState(
                resourceUiState = character,
                successView = { Text(text = it.name) },
                loadingView = { Text(text = "....") },
                onCheckAgain = {},
                onTryAgain = {}
            )
        },
        navigationIcon = { ArrowBackIcon(onBackPressed) }
    )
}