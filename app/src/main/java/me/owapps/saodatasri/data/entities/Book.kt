package me.owapps.saodatasri.data.entities

data class Book(
    val _id: String,
    val bookName: String,
    val description: String,
    val position: Int,
    val raws: List<Raw>
)