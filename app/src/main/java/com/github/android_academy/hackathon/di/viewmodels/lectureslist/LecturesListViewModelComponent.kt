package com.github.android_academy.hackathon.di.viewmodels.lectureslist

import com.bsquaredwifi.app.di.scopes.VM
import com.github.android_academy.hackathon.di.AppComponent
import com.github.android_academy.hackathon.ui.courselist.CourseListViewModel
import com.github.android_academy.hackathon.ui.lectureslist.LecturesListViewModel
import dagger.Component

@VM
@Component(dependencies = [AppComponent::class])
interface LecturesListViewModelComponent {
    fun provideViewModel(): LecturesListViewModel
}