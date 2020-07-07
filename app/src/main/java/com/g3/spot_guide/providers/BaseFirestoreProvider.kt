package com.g3.spot_guide.providers

import com.g3.spot_guide.enums.FirestoreEntityName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

abstract class BaseFirestoreProvider(private val collection: FirestoreEntityName) {

    protected val collectionReference: CollectionReference
        get() = db.collection(collection.collectionName)

    protected val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    protected val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
}