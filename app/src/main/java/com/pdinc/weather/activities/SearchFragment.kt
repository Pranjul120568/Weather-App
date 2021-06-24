package com.pdinc.weather.activities

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pdinc.weather.R
import com.pdinc.weather.adapter.searchCityAdapter
import com.pdinc.weather.databinding.FragmentSearchBinding
import com.pdinc.weather.models.CurrentWeather
import com.pdinc.weather.models.weatherDescription
import com.pdinc.weather.network.WeatherRemoteDataSourceImpl
import com.pdinc.weather.utils.Result
import kotlinx.coroutines.*
import retrofit2.Response

class SearchFragment : Fragment() {
    private val searchName = WeatherRemoteDataSourceImpl()
    private lateinit var searchItem1:SearchView
    private lateinit var binding:FragmentSearchBinding
    private val search_adapter=searchCityAdapter()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val k= inflater.inflate(R.layout.fragment_search, container, false)
        return k
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentSearchBinding.bind(view)
        searchItem1=binding.searchPlace
        searchItem1.onActionViewExpanded()

        searchItem1.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.trim().isNotEmpty()) {
                    runBlocking { searchByName(query) }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {

                }
                return true
            }

        })
        //searchView1 = requireView().findViewById(R.id.searchPlace)
    }
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.navigate, menu)
        }

        private suspend fun searchByName(query: String): Response<CurrentWeather> {
            val re:Response<CurrentWeather>
            withContext(Dispatchers.IO) {
               re= searchName.getCurrentWeatherBySearch(query)
            }
            if(re.body()!=null) {
                Log.d("Data Fetched","Presented")
                setData(re)
            }else{
                Log.d("Data Fetched","Presented")
                Toast.makeText(requireContext(),"Please enter valid city name!",Toast.LENGTH_SHORT).show()
            }
            return re
        }

        private fun setData(it: Response<CurrentWeather>) {
            GlobalScope.launch(Dispatchers.Main) {
                                 binding.placeNameTv.text = it.body()?.name
                                 binding.tempTv.text = it.body()?.main?.temp.toString()
                                 binding.llofplaceitem.isVisible = true
                             }
        }

        private suspend fun searchByGps(lon: Double, lat: Double) {
            searchName.getCurrentWeatherByGps(longitude = lon, latitude = lat)
        }
    }


//                    runBlocking {
//                         searchByName(searchView1.query.toString()).let {
//                             GlobalScope.launch(Dispatchers.Main) {
//                                 binding.placeNameTv.text = it.body()?.name
//                                 binding.tempTv.text = it.body()?.main?.temp.toString()
//                                 binding.llofplaceitem.isVisible = true
//                             }
//                         }
//                        }
//                    }