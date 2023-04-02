package com.example.wifissid

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private var wifiList :ListView? = null
    private var wifiManager :WifiManager? = null
    private val MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1

    var reciverWifi:WifiReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifiList = findViewById(R.id.wifiList)
        val btnScan = findViewById<Button>(R.id.scanBtn)

        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (!wifiManager!!.isWifiEnabled) {
            Toast.makeText(this,"Turing Wifi On..",Toast.LENGTH_LONG).show()
            wifiManager!!.setWifiEnabled(true)
        }
        /*if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),MY_PERMISSIONS_ACCESS_COARSE_LOCATION)
        }*/
        btnScan.setOnClickListener {
            wifiManager!!.startScan()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        reciverWifi = WifiReceiver(wifiManager!!,wifiList!!)
        val intentFilter = IntentFilter()
        intentFilter.addAction(
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
        )
        registerReceiver(reciverWifi,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(reciverWifi)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show()
        }
    }
}
