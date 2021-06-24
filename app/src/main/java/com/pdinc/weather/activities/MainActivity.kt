package com.pdinc.weather.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pdinc.weather.R
import com.pdinc.weather.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomnavigationview:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startTransactionDefaultFragment()
        bottomnavigationview=binding.bottomnavigationview
        val menu: Menu = bottomnavigationview.getMenu()
        val item=menu.findItem(R.id.homeFragment)
        val item1=menu.findItem(R.id.searchFragment)
        item.setOnMenuItemClickListener {
            startTransactionDefaultFragment()
        }
        item1.setOnMenuItemClickListener {
            startTransactionSearchFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.searchFragment -> startTransactionSearchFragment()
            else -> startTransactionDefaultFragment()
        }
    }

    private fun startTransactionDefaultFragment(): Boolean {
     supportFragmentManager.beginTransaction()
             .replace(R.id.fragmentV, DetailsFragment())
             .commit()
        return true
    }

    private fun startTransactionSearchFragment(): Boolean {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentV, SearchFragment())
                .commit()
        return true
    }



}