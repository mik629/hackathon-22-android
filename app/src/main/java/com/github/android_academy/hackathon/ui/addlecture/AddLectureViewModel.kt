package com.github.android_academy.hackathon.ui.addlecture

import android.util.Log
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.github.android_academy.hackathon.Screens
import com.github.android_academy.hackathon.domain.OperationResult
import com.github.android_academy.hackathon.domain.models.Lecture
import com.github.android_academy.hackathon.domain.repositories.CourseRepository
import com.github.android_academy.hackathon.ui.ViewState
import com.github.android_academy.hackathon.ui.addcourse.SingleLiveEvent
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AddLectureViewModel @Inject constructor(
    private val router: Router,
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val mSingleLiveEvent = SingleLiveEvent<ViewState<Unit, String>>()
    val singleLiveEvent: LiveData<ViewState<Unit, String>> get() = mSingleLiveEvent

    fun addLecture(lecture: Lecture) {
        viewModelScope.launch {
            mSingleLiveEvent.value = ViewState.loading()
            val updateResult = courseRepository.updateLecture(lecture)
            Timber.d(updateResult.toString())
            when (updateResult) {
                is OperationResult.Error -> mSingleLiveEvent.value =
                    ViewState.error(updateResult.data.orEmpty())
                is OperationResult.Success -> exitFragment()
            }
        }
    }

    fun exitFragment() {
        router.exit()
    }

    fun startDatePicker(calendar: Calendar){
        router.navigateTo(Screens.datePickerFragment(calendar))
    }
}

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private val TAG = "SingleLiveEvent"
    }
}
