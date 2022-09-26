package com.javacat.easybudget.domain.models

class Category(
    val name: String,
    val pic: Int,
    val type: Type
) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}