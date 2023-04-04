package com.example.listcontacts.utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

object Utils {

    fun showToast(context: Context, message: String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    fun formatPhoneNumber(phoneNumber: String): String {
        phoneNumber.replace("\\D+".toRegex(), "").chunked(2)
        return "(" + phoneNumber[0] + ") " + phoneNumber[1] + "-" + phoneNumber[2] + phoneNumber[3]
    }

    fun loadImageFromUrl(url: String?, circleView: CircleImageView) {
        if (url != null) {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(circleView.context)
                    .load(uri)
                    .into(circleView)
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Falha ao obter URL de download da imagem", exception)
            }
        }
    }


}