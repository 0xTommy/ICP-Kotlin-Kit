package com.bity.icp_kotlin_kit.domain.use_case.transaction

import com.bity.icp_kotlin_kit.domain.factory.TokenRepositoryFactory
import com.bity.icp_kotlin_kit.domain.model.ICPTokenTransfer
import com.bity.icp_kotlin_kit.domain.model.arg.ICPTokenTransferArgs
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger

class SendToken internal constructor(
    private val tokenRepositoryFactory: TokenRepositoryFactory
) {

    suspend operator fun invoke(transferArgs: ICPTokenTransferArgs): Result<ICPTokenTransfer> {
        val repository = tokenRepositoryFactory.createRepository(
            standard = transferArgs.token.standard,
            canister = transferArgs.token.canister
        )
        return try {
            val transfer = repository.transfer(transferArgs)
            Result.success(transfer)
        } catch (t: Throwable) {
            ICPKitLogger.logError(
                "Failed to send token ${transferArgs.token.canister}",
                t
            )
            Result.failure(t)
        }
    }

}