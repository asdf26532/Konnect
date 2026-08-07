package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.han.tripmate.data.model.Comment
import com.han.tripmate.data.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    init {
        fetchPosts()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        fetchPosts()
    }

    fun fetchPosts() {
        var query: Query = db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)

        if (_selectedCategory.value != "전체") {
            query = query.whereEqualTo("category", _selectedCategory.value)
        }

        query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val list = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(id = doc.id)
            }
            _posts.value = list
        }
    }

    fun createPost(
        authorUid: String,
        authorName: String,
        content: String,
        category: String,
        imageUrl: String = ""
    ) {
        if (content.isBlank()) return

        val newPost = hashMapOf(
            "authorUid" to authorUid,
            "authorName" to authorName,
            "authorProfileUrl" to "",
            "content" to content.trim(),
            "category" to category,
            "imageUrl" to imageUrl,
            "likeCount" to 0,
            "likedBy" to emptyList<String>(),
            "commentCount" to 0,
            "timestamp" to Timestamp.now()
        )

        db.collection("posts").add(newPost)
    }

    fun toggleLike(postId: String, currentUid: String, isCurrentlyLiked: Boolean) {
        val postRef = db.collection("posts").document(postId)

        if (isCurrentlyLiked) {
            postRef.update(
                "likeCount", FieldValue.increment(-1),
                "likedBy", FieldValue.arrayRemove(currentUid)
            )
        } else {
            postRef.update(
                "likeCount", FieldValue.increment(1),
                "likedBy", FieldValue.arrayUnion(currentUid)
            )
        }
    }

    fun listenForComments(postId: String) {
        db.collection("posts")
            .document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                }
                _comments.value = list
            }
    }

    fun addComment(postId: String, authorUid: String, authorName: String, content: String) {
        if (content.isBlank()) return

        val newComment = hashMapOf(
            "authorUid" to authorUid,
            "authorName" to authorName,
            "content" to content.trim(),
            "timestamp" to Timestamp.now()
        )

        val postRef = db.collection("posts").document(postId)

        db.runTransaction { transaction ->
            val commentRef = postRef.collection("comments").document()
            transaction.set(commentRef, newComment)
            transaction.update(postRef, "commentCount", FieldValue.increment(1))
        }
    }
}