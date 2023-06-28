package it.dario.malaapplicazione.domain.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.dario.malaapplicazione.data.Constants.FIREBASE_URL
import it.dario.malaapplicazione.data.model.LinkSection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepository(
    private val firebaseDatabase: FirebaseDatabase =
        FirebaseDatabase.getInstance(FIREBASE_URL),
) {

    @ExperimentalCoroutinesApi
    fun fetchLinks() = callbackFlow<Result<List<LinkSection>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(LinkSection::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }
        }
        firebaseDatabase.getReference("links")
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.getReference("links")
                .removeEventListener(postListener)
        }
    }
}
