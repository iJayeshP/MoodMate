package com.jprojects.moodmate.domain.repositoryimpl.moodRepoImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jprojects.moodmate.data.repository.moodRepo.MoodRepository
import com.jprojects.moodmate.domain.model.MoodDetails
import com.jprojects.moodmate.domain.model.UserDetails
import com.jprojects.moodmate.util.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MoodRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseDatabase: FirebaseDatabase
) : MoodRepository {

    override suspend fun addMood(mood: String, moodReason: String): Result<Unit> {
        return try {
            val moodDetails = hashMapOf<String, Any>(
                "mood" to mood,
                "moodReason" to moodReason,
                "userUUID" to firebaseAuth.currentUser?.uid.toString(),
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection(Constants.MOOD_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString())
                .collection(Constants.MOOD_ENTRY)
                .add(moodDetails)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchMood(): Flow<List<MoodDetails>> = callbackFlow {
        val userUid = firebaseAuth.currentUser?.uid
        if (userUid == null) {
            close()
            return@callbackFlow
        }

        val moodRef = firestore.collection(Constants.MOOD_COLLECTION)
            .document(userUid)
            .collection(Constants.MOOD_ENTRY)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val listener = moodRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val moodList =
                    snapshot.documents.mapNotNull { it.toObject(MoodDetails::class.java) }
                trySend(moodList)
            } else {
                trySend(emptyList())
            }
        }

        awaitClose { listener.remove() }
    }


    override suspend fun fetchUserDetails(): Flow<UserDetails> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            close()
            return@callbackFlow
        }

        val userRef = firebaseDatabase.reference.child(Constants.USER_COLLECTION).child(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(UserDetails::class.java)?.let {
                    trySend(it).isSuccess
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        userRef.addValueEventListener(listener)
        awaitClose { userRef.removeEventListener(listener) }
    }

}
