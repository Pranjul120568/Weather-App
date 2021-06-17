package com.pdinc.weather.activities

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.pdinc.weather.R
import com.pdinc.weather.databinding.FragmentSearchBinding
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var bindings: FragmentSearchBinding
    private val searchName =WeatherRemoteDataSourceImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindings = view?.let { FragmentSearchBinding.bind(it) }!!
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.navigate,menu)
        val searchItem=menu.findItem(R.id.searchPlace)
        val searchView=searchItem.actionView as SearchView
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query!=null && query.trim().isNotEmpty()){
                        GlobalScope.launch(Dispatchers.IO) {
                            searchByName(query)
                        }
                    }
                    return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.IO) {
                            searchByName(newText)
                    }
                }
                return false
            }
        })
    }

    private suspend fun searchByName(query: String) {
     searchName.getWeatherBySearch(query)
    }

    companion object {

    }
}