package com.bity.app.ui.screen.tokens_balance

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bity.icp_kotlin_kit.domain.model.ICPPrincipal
import com.bity.icp_kotlin_kit.domain.model.enum.ICPTokenStandard
import com.bity.icp_kotlin_kit.domain.use_case.token.FetchTokensBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TokensBalanceViewModel(
    private val fetchTokensBalance: FetchTokensBalance
): ViewModel() {

    var state: TokensBalanceState by mutableStateOf(TokensBalanceState.TokenWithBalance())
        private set

    fun getTokens(principal: String) {
        state = TokensBalanceState.Loading
        viewModelScope.launch {
            try {
                val tokens = withContext(Dispatchers.IO) {
                    fetchTokensBalance(ICPPrincipal(principal))
                }
                state = TokensBalanceState.TokenWithBalance(
                    tokens.filter { it.token.standard != ICPTokenStandard.ICP }
                )
            } catch (t: Throwable) {
                Log.e(TAG, "getTokens: ", t)
                state = TokensBalanceState.Error(t.message)
            }
        }
    }

    companion object {
        private const val TAG = "TokensBalanceViewModel"
    }
}