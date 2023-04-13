package com.example.listcontacts.dao

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.listcontacts.R
import com.example.listcontacts.database.FirebaseDao.Companion.getDatabase
import com.example.listcontacts.database.FirebaseDao.Companion.getFunctions
import com.example.listcontacts.database.FirebaseDao.Companion.getIdUser
import com.example.listcontacts.databinding.FragmentListBinding
import com.example.listcontacts.model.Contact
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ContactDao {

    private lateinit var storageReference: StorageReference


//    fun getAll(callback: (List<Contact>) -> Unit) {
//        val uid = hashMapOf(
//            "uid" to getIdUser().toString()
//        )
//        getContactsById.call(uid)
//            .addOnSuccessListener { result ->
//                val contactsMap = result.data as Map<String, Any>
//                val contacts = mutableListOf<Contact>()
//                val gson = Gson()
//                for (contactData in contactsMap.values) {
//                    val contact = gson.fromJson(gson.toJson(contactData), Contact::class.java)
//                    contacts.add(contact)
//                }
//                callback(contacts)
//            }
//            .addOnFailureListener { exception ->
//                callback(emptyList())
//            }
//    }

//    fun getAll(callback: (List<Contact>) -> Unit) {
//        val uid = hashMapOf(
//            "uid" to getIdUser().toString()
//        )
//        getContacts.call(uid)
//            .addOnSuccessListener { result ->
//                // Handle success
//                val contacts = result.data as List<Contact>
//                callback(contacts)
//            }
//            .addOnFailureListener { exception ->
//                // Handle failure
//                callback(emptyList())
//            }
//
//    }
    fun getAll(callback: (List<Contact>) -> Unit) {
        getDatabase()
            .collection("USERS")
            .document(getIdUser().toString())
            .collection("CONTACTS")
            .get()
            .addOnSuccessListener { result ->
                val contacts = mutableListOf<Contact>()
                for (document in result) {
                    val contact = document.toObject(Contact::class.java)
                    contacts.add(contact)
                }
                callback(contacts)
            }
            .addOnFailureListener { exception ->
                callback(emptyList())
            }
    }

//    fun getAll(callback: (List<Contact>) -> Unit) {
//        val uid = hashMapOf(
//            "uid" to getIdUser().toString()
//        )
//        getContactsById.call(uid)
//            .addOnSuccessListener { result ->
//                Log.d("ContactJson", "oi")
//                //val contactsJson = result.data?.getJSONArray("contacts")
//                val contactsJson = result.data as? JSONArray
//                Log.d("ContactJson", "oi")
//                val contacts = mutableListOf<Contact>()
//                val gson = Gson()
//                if (contactsJson != null) {
//                    for (i in 0 until contactsJson.length()) {
//                        val contactJson = contactsJson.getJSONObject(i)
//                        val contact = gson.fromJson(contactJson.toString(), Contact::class.java)
//                        Log.d("Test3",contact.toString())
//                        contacts.add(contact)
//                    }
//                }
//                callback(contacts)
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Log 12", "sdsds")
//                Log.d("Error message", exception.message.toString())
//
//                // Handle failure
//                callback(emptyList())
//            }
//    }
    val getContactsById = FirebaseFunctions.getInstance().getHttpsCallable("getContactsById")
//    fun getAll(callback: (List<Contact>) -> Unit) {
//        val uid = hashMapOf(
//            "uid" to getIdUser().toString()
//        )
//        getContactsById.call(uid)
//            .addOnSuccessListener { result ->
//                Log.d("ContactJson", "oi")
//                val contactsJson = result.data as? JSONObject // Atualizado para analisar um objeto JSON
//                Log.d("ContactJson", "oi")
//                val contacts = mutableListOf<Contact>()
//                val gson = Gson()
//                if (contactsJson != null) {
//                    val contactsJsonArray = contactsJson.getJSONArray("contacts")
//                    for (i in 0 until contactsJsonArray.length()) {
//                        val contactJson = contactsJsonArray.getJSONObject(i)
//                        val contact = gson.fromJson(contactJson.toString(), Contact::class.java)
//                        Log.d("Test3",contact.toString())
//                        contacts.add(contact)
//                    }
//                }
//                callback(contacts)
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Log 12", "sdsds")
//                Log.d("Error message", exception.message.toString())
//
//                // Handle failure
//                callback(emptyList())
//            }
//    }

//    fun getAll(callback: (List<Contact>) -> Unit) {
//        val uid = hashMapOf(
//            "uid" to getIdUser().toString()
//        )
//
//        val url = "https://southamerica-east1-listcontacts-bfb5e.cloudfunctions.net/getContactsById"
//
//        val jsonBody = JSONObject(uid as Map<*, *>?)
//
//        val request = JsonObjectRequest(
//            Request.Method.POST,
//            url,
//            jsonBody,
//            { response ->
//                val contactsJson = response.getJSONArray("contacts")
//                val contacts = mutableListOf<Contact>()
//                val gson = Gson()
//
//                for (i in 0 until contactsJson.length()) {
//                    val contactJson = contactsJson.getJSONObject(i)
//                    val contact = gson.fromJson(contactJson.toString(), Contact::class.java)
//                    contacts.add(contact)
//                }
//
//                callback(contacts)
//            },
//            { error ->
//                Log.d("Error message", error.message.toString())
//                callback(emptyList())
//            }
//        )
//
//
//    }

    fun addContact(contact: Contact, onSuccess: () -> Unit, onFailure: (Exception) -> Unit,bitmap: Bitmap?) {
        getDatabase()
            .collection("USERS")
            .document(getIdUser().toString())
            .collection("CONTACTS")
            .document(contact.id)
            .set(contact)
            .addOnSuccessListener { documentReference ->
                uploadProfile(bitmap, contact.id).addOnSuccessListener { url ->
                    getDatabase()
                        .collection("USERS")
                        .document(getIdUser().toString())
                        .collection("CONTACTS")
                        .document(contact.id)
                        .update("imgUrl",url.toString())
                    onSuccess()
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteContact(contact: Contact, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        getDatabase()
            .collection("USERS")
            .document(getIdUser().toString())
            .collection("CONTACTS")
            .document(contact.id)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun editContact(contact: Contact, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        getDatabase()
            .collection("USERS")
            .document(getIdUser().toString())
            .collection("CONTACTS")
            .document(contact.id)
            .set(contact)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private fun uploadProfile(img: Bitmap?,contactId: String): Task<Uri> {
        val baos = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageReference =
            FirebaseStorage.getInstance().getReference("CONTACTS/PHOTOS/$contactId")
        val uploadTask = storageReference.putBytes(data)
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageReference.downloadUrl
        }
    }

    companion object {
        private val contacts = mutableListOf<Contact>()
    }
}