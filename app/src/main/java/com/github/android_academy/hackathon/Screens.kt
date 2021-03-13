package com.github.android_academy.hackathon

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.android_academy.hackathon.ui.courselist.CourseListFragment
import com.github.android_academy.hackathon.ui.login.LoginFragment
import com.github.android_academy.hackathon.ui.registration.RegistrationFragment
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.github.android_academy.hackathon.ui.addcourse.AddCourseFragment

object Screens {
    fun LoginFragment(): FragmentScreen =
        FragmentScreen(
            fragmentCreator = FragmentCreator(LoginFragment.newInstance())
        )

    fun RegistrationFragment(): FragmentScreen =
        FragmentScreen(
            fragmentCreator = FragmentCreator(RegistrationFragment.newInstance())
        )

    fun RegistrationFragment(login:String, password : String): FragmentScreen =
        FragmentScreen(
            fragmentCreator = FragmentCreator(RegistrationFragment.newInstance(login, password))
        )
    fun CourseListFragment(): FragmentScreen =
            FragmentScreen(
                    fragmentCreator = FragmentCreator(CourseListFragment.newInstance())
            )

    fun AddCourseFragment() : FragmentScreen =
        FragmentScreen(
            fragmentCreator = FragmentCreator(AddCourseFragment.newInstance())
        )

    class FragmentCreator(private val fragment: Fragment) : Creator<FragmentFactory, Fragment> {
        override fun create(argument: FragmentFactory): Fragment =
            fragment
    }
}
