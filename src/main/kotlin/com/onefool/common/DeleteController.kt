package com.onefool.common

import java.io.Serializable


/**
 * @Author linjiawei
 * @Date 2024/3/21 16:52
 */
interface DeleteController<T> {

    fun deleteOne(id : Serializable) : Unit
}