package com.bity.icp_kotlin_kit.domain.service

internal class DefaultPaginationStrategy : PaginationStrategy {
    override fun calculatePages(totalCount: ULong, pageSize: ULong): ULong {
        return (totalCount / pageSize) + if (totalCount % pageSize > 0UL) 1UL else 0UL
    }

    override fun getPageIndices(totalPages: ULong): List<ULong> {
        return (0UL until totalPages).toList()
    }
}