package com.example.listcontacts.dao

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.view.isVisible
import com.example.listcontacts.R
import com.example.listcontacts.database.FirebaseDao.Companion.getDatabase
import com.example.listcontacts.database.FirebaseDao.Companion.getIdUser
import com.example.listcontacts.databinding.FragmentListBinding
import com.example.listcontacts.model.Contact
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ContactDao {

    private lateinit var storageReference: StorageReference

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