package com.smokezone.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

val auth: FirebaseAuth = Firebase.auth

val firebasedb = FirebaseDatabase.getInstance("https://smokezone-25546-default-rtdb.asia-southeast1.firebasedatabase.app")

val firestore = FirebaseFirestore.getInstance()