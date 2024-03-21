package com.onefool.dto

import java.io.Serializable

/**
 * @Author linjiawei
 * @Date 2024/3/21 17:41
 */
class PageRequestDto<T>(
    private var page: Long = 1,
    private var size: Long = 10,
    private var record: T
) : Serializable {

    fun getPage(): Long {
        return page
    }

    fun getSize(): Long {
        return size
    }

    fun getRecord(): T {
        return record
    }
    fun setPage(page: Long) {
        this.page = page
    }
    fun setSize(size: Long) {
        this.size = size
    }
    fun setRecord(record: T) {
        this.record = record
    }
}