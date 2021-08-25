package com.mco.mchat.firebase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseStorageSource: KoinComponent {
    private val storageInstance: FirebaseStorage by inject()

    fun uploadUserImage(userID: String, bArr: ByteArray): Task<Uri> {
        val path = "user_photos/$userID/profile_image"
        val ref = storageInstance.reference.child(path)

        return ref.putBytes(bArr).continueWithTask {
            ref.downloadUrl
        }
    }
}