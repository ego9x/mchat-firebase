package com.mco.mchat.data.repo

import android.net.Uri
import com.mco.mchat.firebase.FirebaseStorageSource
import com.mco.mchat.data.Result


class StorageRepository(private val firebaseStorageService: FirebaseStorageSource) {


    fun updateUserProfileImage(userID: String, byteArray: ByteArray, b: (Result<Uri>) -> Unit) {
        b.invoke(Result.Loading)
        firebaseStorageService.uploadUserImage(userID, byteArray).addOnSuccessListener {
            b.invoke((Result.Success(it)))
        }.addOnFailureListener {
            b.invoke(Result.Error(it.message))
        }
    }
}