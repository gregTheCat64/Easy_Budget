package com.javacat.easybudget.domain.models

data class Category(
    var id: Int,
    val name: String,
    val pic: Int,
    val type: Type
) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}