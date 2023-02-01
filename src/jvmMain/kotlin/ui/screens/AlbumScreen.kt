package ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ui.component.AlbumListRequester
import ui.component.MyTopAppBar
import model.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(viewModel: SharedViewModel, onLogout: ()->Unit){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { MyTopAppBar(scrollBehavior = scrollBehavior, onLogout = onLogout) }
    ){
        AlbumListRequester(paddingValues = it, viewModel = viewModel)
    }
}