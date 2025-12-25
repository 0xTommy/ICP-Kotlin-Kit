package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.model.request.TransferICPRequest
import com.bity.icp_kotlin_kit.domain.repository.LedgerCanisterRepository
import java.math.BigInteger

class SendICP internal constructor(
    private val repository: LedgerCanisterRepository
) {

    suspend operator fun invoke(request: TransferICPRequest) : Result<BigInteger> {
        return try {
            val result = repository.transferICP(request)
            Result.success(result)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}