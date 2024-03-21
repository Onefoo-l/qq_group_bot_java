package com.onefool.dto

import java.io.Serializable

/**
 * @Author linjiawei
 * @Date 2024/3/21 17:48
 */
class PageInfo<T>(private var page : Long,
    private var size : Long ,
    private var total : Long,
    private var totalPages : Long,
    private var list : List<T>) : Serializable {
}