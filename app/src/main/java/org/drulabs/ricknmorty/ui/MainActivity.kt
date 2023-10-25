package org.drulabs.ricknmorty.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.drulabs.ricknmorty.ui.characteritemtype.CharacterItemTypeScreen
import org.drulabs.ricknmorty.ui.characteritemtype.CharacterViewModel
import org.drulabs.ricknmorty.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val characterViewModel: CharacterViewModel by viewModels()

    @ExperimentalComposeUiApi
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                    Column {
                        CharacterItemTypeScreen(
                            viewModel = characterViewModel
                        ) {
                            launchURL(this@MainActivity, it.url)
                        }
                    }
                }
            }
        }
    }
}

fun launchURL(context: Context, url: String) {
    Intent(Intent.ACTION_VIEW).let {
        it.data = Uri.parse(url)
        context.startActivity(it)
    }
}