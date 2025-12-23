package com.bity.icp_kotlin_kit.domain.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ParallelFetchStrategy : ConcurrentFetchStrategy {
    override suspend fun <T> fetchAll(
        items: List<ULong>,
        fetcher: suspend (ULong) -> T
    ): List<T> = coroutineScope {
        items.map { item ->
            async { fetcher(item) }
        }.awaitAll()
    }
}