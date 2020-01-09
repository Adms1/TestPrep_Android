package com.testcraft.testcraft

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkSchedulerService : JobService(),
    Connectivity.Companion.ConnectivityReceiverListener {
    private val TAG = NetworkSchedulerService::class.java.simpleName

    private var mConnectivityReceiver: Connectivity? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        mConnectivityReceiver = Connectivity()
    }


    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return Service.START_NOT_STICKY
    }


    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStartJob$mConnectivityReceiver")
        registerReceiver(mConnectivityReceiver, IntentFilter("coonnecctivity"))
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStopJob")
        unregisterReceiver(mConnectivityReceiver)
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val message =
            if (isConnected) "Good! Connected to Internet" else "Sorry! Not connected to internet"
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
