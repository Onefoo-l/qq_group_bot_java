package com.onefool.common

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.onefool.dto.PageInfo
import com.onefool.dto.PageRequestDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.beans.PropertyDescriptor
import java.io.Serializable
import kotlin.Exception


/**
 * @Author linjiawei
 * @Date 2024/3/21 16:54
 */
class AbstractController<T : Any> : GeneralController<T>{

    //日志
    companion object{
        val logger : Logger = LoggerFactory.getLogger(AbstractController::class.java);
    }

    protected lateinit var coreService : IService<T>

    constructor(coreService : IService<T>){
        this.coreService = coreService
    }

    fun setContructor(coreService: IService<T>){
        this.coreService = coreService
    }

    /**
     * 根据条件查询
     */
    override fun findByRecord(record : T) : MutableList<T> {
        val queryWrapper : QueryWrapper<T> = QueryWrapper()
        val declaredField  = record!!::class.java.getDeclaredFields()
        declaredField.forEach {d ->
            val annotation : TableField = d.getAnnotation(TableField::class.java)
            try {
                d.isAccessible = true
                val value = d.get(record)
                if (value != null) queryWrapper.eq(annotation.value,value)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        val list : MutableList<T> = coreService.list(queryWrapper)
        return  list
    }

    /**
     * 分页查询
     */
    override fun findPageInfo(records: PageRequestDto<T>): PageInfo<T> {
        val page = Page<T>(records.getPage(),records.getSize())

        val querywraaper : QueryWrapper<T> = getWrapper(records.getRecord())
        val iPage = coreService.page(page, querywraaper)
        val tPage : PageInfo<T> = PageInfo(iPage.current,iPage.size,iPage.total,iPage.pages,iPage.records)
        return tPage
    }

    fun getWrapper(body : T) : QueryWrapper<T>{
        var queryWrapper : QueryWrapper<T> = QueryWrapper()
        if (body == null) return queryWrapper
        val declaredFields = body::class.java.getDeclaredFields()
        declaredFields.forEach { d ->
            try {
                if (d.isAnnotationPresent(TableId::class.java) || d.name.equals("serialVersionUID")) {
                    return@forEach
                }

                val property : PropertyDescriptor = PropertyDescriptor(d.name,body::class.java)
                val value = property.readMethod.invoke(body)
                val annotation = d.getAnnotation(TableField::class.java)

                if (value != null){
                    if (value.javaClass.equals("java.lang.String")) {
                        queryWrapper.like(annotation.value,value)
                    }else {
                        queryWrapper.eq(annotation.value,value)
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
                logger.error("获取字段失败")
            }
        }

        return queryWrapper
    }

    /**
     * 删除
     */
    override fun deleteOne(id : Serializable) : Unit {
        var flag = coreService.removeById(id)
    }
}