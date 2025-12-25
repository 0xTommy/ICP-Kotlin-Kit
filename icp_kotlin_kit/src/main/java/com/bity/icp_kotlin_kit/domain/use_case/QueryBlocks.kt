package com.bity.icp_kotlin_kit.domain.use_case

import com.bity.icp_kotlin_kit.domain.exception.ICPKitException
import com.bity.icp_kotlin_kit.domain.model.icp_block.ICPBlock
import com.bity.icp_kotlin_kit.domain.repository.LedgerCanisterRepository

class QueryBlocks internal constructor(
    private val repository: LedgerCanisterRepository
) {

    suspend operator fun invoke(start: ULong, length: ULong): Result<List<ICPBlock>> {
        return try {
            val blocks = repository.queryBlocks(start, length)
            if(blocks.size != length.toInt()) Result.failure(
                ICPKitException.InvalidBlocksLength(
                    expected = length.toInt(),
                    returned = blocks.size
                )
            )
            else Result.success(blocks)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}