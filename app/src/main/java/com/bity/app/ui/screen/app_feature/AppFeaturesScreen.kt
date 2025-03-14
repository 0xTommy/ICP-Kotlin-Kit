package com.bity.app.ui.screen.app_feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bity.app.ui.util.Screen

@Composable
fun AppFeatures(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            text = "App features",
            style = MaterialTheme.typography.titleLarge
        )
        MainButton(text = "ICP Tokens") {
            navController.navigate(Screen.ICPTokens.route)
        }
        MainButton(text = "Account ICP Tokens Balance") {
            navController.navigate(Screen.AccountBalance.route)
        }
        MainButton(text = "ICP NFTs") {
            navController.navigate(Screen.ICPNFTs.route)
        }
        MainButton(text = "Account NFT holdings") {
            navController.navigate(Screen.NFTHoldings.route)
        }
    }
}

@Composable
private fun MainButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .width(180.dp),
        onClick = { onClick() }
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMainButton() {
    MainButton(
        text = "Account Balance",
        onClick = { }
    )
}