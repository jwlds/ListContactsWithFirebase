package com.example.listcontacts.model

import android.os.Parcelable
import com.example.listcontacts.database.FirebaseDao
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    var id: String = "",
    val name: String = "",
    val numberPhone: String = "",
    val imgUrl: String? = null,
    val userId: String = ""
) : Parcelable {
    init {
        this.id = FirebaseDao.getDatabase().collection("contacts").document().id
    }
}

