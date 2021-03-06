package com.github.android_academy.hackathon.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.android_academy.hackathon.Screens
import com.github.android_academy.hackathon.domain.OperationResult
import com.github.android_academy.hackathon.domain.repositories.AuthRepository
import com.github.android_academy.hackathon.ui.ViewState
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val router : Router
) : ViewModel() {

    private val mutableRegistrationResult: MutableLiveData<ViewState<Unit, String?>> =
        MutableLiveData()

    val registrationResult: LiveData<ViewState<Unit, String?>> get() = mutableRegistrationResult

    fun register(
        username: String,
        password: String,
        name: String,
        isMentor: Boolean
    ) {
        viewModelScope.launch {
            mutableRegistrationResult.value = ViewState.loading()

            val result = authRepository.register(
                    username,
                    password,
                    name,
                    isMentor
                )
            mutableRegistrationResult.value = when(result){
                is OperationResult.Error -> ViewState.error(result.data)
                is OperationResult.Success -> ViewState.success(result.data)
            }
        }
    }

    fun goToPreviousFragment(){
        router.exit()
    }

    fun launchCourseList(){
        router.navigateTo(Screens.courseListFragment())
    }
}