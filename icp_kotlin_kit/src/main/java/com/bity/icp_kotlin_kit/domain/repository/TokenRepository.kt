package com.bity.icp_kotlin_kit.domain.repository

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.model.ICPTokenTransfer
import com.bity.icp_kotlin_kit.domain.model.arg.ICPTokenTransferArgs
import java.math.BigInteger

internal interface TokenRepository {
    suspend fun getTokenCount(): ULong
    suspend fun fetchTokensPage(startAt: ULong, pageSize: ULong): List<ICPToken>
    suspend fun fee(token: ICPToken): BigInteger
    suspend fun send(transferArgs: ICPTokenTransferArgs): ICPTokenTransfer
}