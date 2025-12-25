package com.bity.icp_kotlin_kit.domain.use_case.token

import com.bity.icp_kotlin_kit.domain.model.ICPToken
import com.bity.icp_kotlin_kit.domain.repository.TokenCache
import com.bity.icp_kotlin_kit.domain.repository.TokenRepository
import com.bity.icp_kotlin_kit.util.logger.ICPKitLogger
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class FetchAllTokens internal constructor(
    private val tokenCache: TokenCache,
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(): Result<List<ICPToken>> {
        tokenCache.get()?.let {
            return Result.success(it)
        }
        return try {
            val totalCount = tokenRepository.getTokenCount()
            val pages = calculatePages(totalCount, PAGE_SIZE)
            val allTokens = fetchAllPages(pages)
            tokenCache.set(allTokens)
            Result.success(allTokens)
        } catch (ex: Exception) {
            ICPKitLogger.logError(throwable = ex)
            Result.failure(ex)
        }
    }

    private suspend fun fetchAllPages(totalPages: ULong): List<ICPToken> {
        return coroutineScope {
            (0UL until totalPages).map { pageIndex ->
                async {
                    fetchPageSafely(pageIndex)
                }
            }.awaitAll()
                .flatten()
        }
    }

    private suspend fun fetchPageSafely(pageIndex: ULong): List<ICPToken> {
        val startAt = pageIndex * PAGE_SIZE
        return try {
            val tokens = tokenRepository.fetchTokensPage(
                startAt = startAt,
                pageSize = PAGE_SIZE
            )
            tokens
        } catch (ex: Exception) {
            ICPKitLogger.logError(
                throwable = ex,
                message = "Failed to fetch page $pageIndex"
            )
            emptyList()
        }
    }

    private fun calculatePages(totalCount: ULong, pageSize: ULong): ULong {
        return if (totalCount == 0UL) {
            0UL
        } else {
            (totalCount / pageSize) + if (totalCount % pageSize > 0UL) 1UL else 0UL
        }
    }

    companion object {
        private const val PAGE_SIZE = 100UL
    }

}