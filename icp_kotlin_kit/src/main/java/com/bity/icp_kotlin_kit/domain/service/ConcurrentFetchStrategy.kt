package com.bity.icp_kotlin_kit.domain.service

interface ConcurrentFetchStrategy {
    suspend fun <T> fetchAll(
        items: List<ULong>,
        fetcher: suspend (ULong) -> T
    ): List<T>
}