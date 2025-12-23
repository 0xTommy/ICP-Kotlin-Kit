package com.bity.icp_kotlin_kit.domain.repository

import com.bity.icp_kotlin_kit.domain.model.ICPToken

internal interface TokenCache {
    fun get(): List<ICPToken>?
    fun set(tokens: List<ICPToken>)
    fun clear()
}