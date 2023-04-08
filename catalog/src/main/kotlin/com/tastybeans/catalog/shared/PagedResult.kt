package com.tastybeans.catalog.shared

data class PagedResult<T>(val items: List<T>, val pageIndex: Int, val pageSize: Int, val total: Long)