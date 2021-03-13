package com.github.android_academy.hackathon.ui.courselist

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.android_academy.hackathon.App
import com.github.android_academy.hackathon.R
import com.github.android_academy.hackathon.databinding.CourseListFragmentBinding
import com.github.android_academy.hackathon.di.viewmodels.courselist.DaggerCourseListViewModelComponent
import com.github.android_academy.hackathon.domain.models.Course
import com.github.android_academy.hackathon.ui.BaseFragment
import com.github.android_academy.hackathon.ui.ViewState

class CourseListFragment : BaseFragment(R.layout.course_list_fragment){
    private val binding by viewBinding(CourseListFragmentBinding::bind)

    private val coursesAdapter = CoursesAdapter(
        courseListener = {viewModel.onCourseAction(it)},
        {viewModel.subscribeAction(it)}
    )

    private val viewModel: CourseListViewModel by viewModels(
            factoryProducer = { CourseListViewModelFactory() }
    )

    override fun initViews(view: View) {
        super.initViews(view)

        binding.courseListFragmentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.courseListFragmentRecycler.adapter = coursesAdapter

        //fab
        if(!viewModel.isMentor()) binding.courseFragmentFab.hide() //спрятать если не ментор
        binding.courseFragmentFab.setOnClickListener {
            viewModel.addCourseAction()
        }

        //TODO observe
        viewModel.courses.observe(viewLifecycleOwner, this::updateAdapter)

        //switch
        binding.courseListFragmentSwitch.setOnCheckedChangeListener { button, b ->
            if (b) viewModel.showFavoriteCourses() else  viewModel.showAllCourses()
        }

        //show courses
        if (binding.courseListFragmentSwitch.splitTrack) viewModel.showFavoriteCourses() else  viewModel.showAllCourses()
    }

    private fun updateAdapter(courses: ViewState<List<Course>, String?>){
        when(courses){
            is ViewState.Success -> coursesAdapter.submitList(courses.result)
            is ViewState.Loading -> {/*TODO показать загрузку */}
            is ViewState.Error -> {
                Toast.makeText(context, "Something went wrong",Toast.LENGTH_LONG).show()
                Log.e("Updating list error", courses.result?: "Null string")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CourseListFragment()
    }
}

private class CourseListViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DaggerCourseListViewModelComponent.builder()
                .appComponent(App.appComponent)
                .build()
                .provideViewModel() as T
    }
}