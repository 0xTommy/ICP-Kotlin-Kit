package com.bity.icp_kotlin_kit.data.repository

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.repository.TokenCache

internal class InMemoryTokenCache() : TokenCache {

    private var cachedTokens: List<ICPToken>? = null

    override fun get(): List<ICPToken>? = cachedTokens

    override fun set(tokens: List<ICPToken>) {
        cachedTokens = tokens
    }

    override fun clear() {
        cachedTokens = null
    }

}