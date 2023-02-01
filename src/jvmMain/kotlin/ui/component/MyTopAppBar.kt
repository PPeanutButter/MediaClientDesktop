package ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, onLogout:()->Unit){
    CenterAlignedTopAppBar(
        title = { AppTitleText() },
        scrollBehavior = scrollBehavior,
        actions = { SettingAction { onLogout() } },
        modifier = Modifier
    )
}