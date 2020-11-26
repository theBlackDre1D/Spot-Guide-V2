package com.g3.spot_guide.extensions

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.g3.spot_guide.models.ImageModel
import com.google.firebase.storage.FirebaseStorage

fun ImageView.loadImageFromUri(uri: Uri?) {
    Glide.with(this)
        .load(uri).
        into(this)
}

fun ImageView.loadImageFromFirebase(path: String?, placeholder: Int? = null, afterLoad: (() -> Unit)? = null) {
    try {
        placeholder?.let { this.setImageResource(it) }
        path?.let { p ->
            if (p.isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(p).getBytes(Long.MAX_VALUE).addOnSuccessListener {
                    try {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        Glide.with(context)
                            .load(bitmap)
                            .into(this)
                        afterLoad?.invoke()
                    } catch (e: Exception) {}
                }.addOnFailureListener {}
            }
        }
    } catch (e: Exception) {}
}

fun ImageView.loadImageFromImageModel(imageModel: ImageModel) {
    if (imageModel.bitmap != null) {
        Glide.with(this)
            .load(imageModel.bitmap).
            into(this)
    }

    if (imageModel.uri != null) {
        Glide.with(this)
            .load(imageModel.uri).
            into(this)
    }
}