package com.mco.mchat.data.repo

import com.google.firebase.auth.FirebaseUser
import com.mco.mchat.data.model.CreateUser
import com.mco.mchat.data.model.Login
import com.mco.mchat.firebase.FirebaseAuthSource
import com.mco.mchat.firebase.FirebaseAuthStateObserver
import com.mco.mchat.data.Result

class AuthRepository(private val firebaseAuthService: FirebaseAuthSource){

    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((Result<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}