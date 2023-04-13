package com.example.listcontacts.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase

class FirebaseDao {


    companion object {
        fun getDatabase() =  FirebaseFirestore.getInstance()

        fun getFunctions() = FirebaseFunctions.getInstance()

        fun getAuth() = Firebase.auth

        fun getIdUser() = getAuth().uid

        fun isAuth() = getAuth().currentUser != null


    }
}