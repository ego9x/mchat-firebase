package com.mco.mchat.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.mco.mchat.R
import com.mco.mchat.databinding.ActivityMainBinding
import com.mco.mchat.firebase.FirebaseDataSource
import com.mco.mchat.utils.DialogHelper
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),KoinComponent {
    private val binding : ActivityMainBinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModel()
    private var loadingDialog: AlertDialog? = null
    private lateinit var notificationsBadge: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.userNotificationsList.observe(this, {
            it?.let {
                if (it.size > 0) {
                    notificationsBadge.number = it.size
                    notificationsBadge.isVisible = true
                } else {
                    notificationsBadge.isVisible = false
                }
            }
        })
    }

    private fun setupView() {
        supportActionBar?.hide()
        with(binding){
            notificationsBadge = navView.getOrCreateBadge(R.id.navNotify).apply { isVisible = false }

            val navController = findNavController(R.id.nav_host_fragment)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.introFragment -> navView.visibility = View.GONE
                    R.id.signInFragment -> navView.visibility = View.GONE
                    R.id.signUpFragment -> navView.visibility = View.GONE
                    R.id.chatBoxFragment -> navView.visibility = View.GONE
                    else -> navView.visibility = View.VISIBLE
                }
            }

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navNotify,
                    R.id.navChats,
                    R.id.navProfile,
                    R.id.introFragment
                )
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

        }
    }

    fun showLoadingDialog() {
        if (loadingDialog == null)
            loadingDialog = DialogHelper.loadingDialog(this)
        loadingDialog?.show()
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        viewModel.firebaseDatabaseService.dbInstance.goOffline()
    }

    override fun onResume() {
        super.onResume()
        viewModel.firebaseDatabaseService.dbInstance.goOnline()
        //setupViewModelObservers()
    }
}