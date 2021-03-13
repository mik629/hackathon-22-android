package com.github.android_academy.hackathon.ui.loginfragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.android_academy.hackathon.App
import com.github.android_academy.hackathon.R
import com.github.android_academy.hackathon.databinding.LoginFragmentBinding
import com.github.android_academy.hackathon.di.viewmodels.login.DaggerLoginViewModelComponent
import com.github.android_academy.hackathon.ui.BaseFragment
import com.github.android_academy.hackathon.ui.ViewState
import com.google.android.material.snackbar.Snackbar

class LoginFragment : BaseFragment(R.layout.login_fragment) {
    private val binding by viewBinding(LoginFragmentBinding::bind)


    private val viewModel: LoginViewModel by viewModels(
        factoryProducer = { LoginViewModelFactory() }
    )

    override fun initViews(view: View) {
        super.initViews(view)
        viewModel.loginResult.observe(viewLifecycleOwner, { checkLoginResult(it) })

        binding.loginFragmentSignInButton.setOnClickListener {
            viewModel.login(
                binding.loginFragmentLogin.editText?.text.toString(),
                binding.loginFragmentPassword.editText?.text.toString()
            )
        }

        binding.loginFragmentSignUpButton.setOnClickListener {
            //TODO запустить фрагмент регистрации и перенести логин с паролем
            viewModel.launchRegistration(
                binding.loginFragmentLogin.editText?.text.toString(),
                binding.loginFragmentPassword.editText?.text.toString()
            )
        }

        bindHideErrors()
    }

    fun checkLoginResult(it: ViewState<Unit, String?>) {
        //it.Success
        when (it) {
            is ViewState.Loading -> {/*TODO показать анимацию */
            }
            is ViewState.Error -> {
                //TODO в зависимости от сообщения подчеркивать разные поля
                wrongPassword(it.result ?: "wrong username or password")
            }
            is ViewState.Success -> {
                //TODO запустить другой фрагмент
                Snackbar.make(binding.root, R.string.success, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun wrongPassword(message: String) {
        binding.loginFragmentPassword.error = message
    }


    fun bindHideErrors() {
        //ошибка исчезнет при изменении текста логина
        binding.loginFragmentLogin.addOnEditTextAttachedListener {
            binding.loginFragmentLogin.error = null
        }

        //ошибка исчезнет при изменении текста пароля
        binding.loginFragmentPassword.addOnEditTextAttachedListener {
            binding.loginFragmentPassword.error = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}

private class LoginViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DaggerLoginViewModelComponent.builder()
            .appComponent(App.appComponent)
            .build()
            .provideViewModel() as T
    }
}