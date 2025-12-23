package com.bity.icp_kotlin_kit.domain.service

internal interface PaginationStrategy {
    fun calculatePages(totalCount: ULong, pageSize: ULong): ULong
    fun getPageIndices(totalPages: ULong): List<ULong>
}