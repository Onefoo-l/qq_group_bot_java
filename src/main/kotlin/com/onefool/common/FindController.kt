package com.onefool.common

import com.onefool.dto.PageInfo
import com.onefool.dto.PageRequestDto

/**
 * @Author linjiawei
 * @Date 2024/3/21 16:51
 */
interface FindController<T> {

    fun findByRecord(record : T) : MutableList<T>

    fun findPageInfo(records : PageRequestDto<T>) : PageInfo<T>
}