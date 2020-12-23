package com.g3.spot_guide.screens.crew

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.Session
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.MemberRequestDecision
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.UserRepository

class CrewFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val crewMembers = MutableLiveData<ArrayList<Either<User>>>()
    val crewMemberRequests = MutableLiveData<ArrayList<Either<User>>>()

    val crewMemberResult = MutableLiveData<Either<MemberRequestDecision>>()

    fun getCrewMembers() {
        doInCoroutine {
            val loggedInUser = Session.loggedInUser
            loggedInUser?.let { user ->
                val obtainedUsers = ArrayList<Either<User>>()
                user.friends.forEach { userId ->
                    val result = userRepository.getUserById(userId)
                    obtainedUsers.add(result)
                }
                crewMembers.postValue(obtainedUsers)
            }
        }
    }

    fun getCrewMemberRequests() {
       doInCoroutine {
            val loggedInUser = Session.loggedInUser
            loggedInUser?.let { user ->
                val obtainedUsers = ArrayList<Either<User>>()
                user.memberRequests.forEach { userId ->
                    val result = userRepository.getUserById(userId)
                    obtainedUsers.add(result)
                }
                crewMemberRequests.postValue(obtainedUsers)
            }
        }
    }

    fun onCrewMemberRequestDecision(accept: Boolean, requestUserId: String) {
        doInCoroutine {
            val result = userRepository.onCrewMemberRequestDecision(accept, requestUserId)
            crewMemberResult.postValue(result)
        }
    }
}