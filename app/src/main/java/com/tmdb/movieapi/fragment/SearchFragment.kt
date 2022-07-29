package com.tmdb.movieapi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tmdb.movieapi.adapter.SearchAdapter
import com.tmdb.movieapi.adapter.SearchAdapterMovie
import com.tmdb.movieapi.databinding.FragmentSearchBinding
import com.tmdb.movieapi.models.Anime
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.utils.Response
import com.tmdb.movieapi.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var popingAnim: Animation
    private lateinit var navController: NavController
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchAdapterMovie: SearchAdapterMovie
    private var isMovie: Boolean = false
    private var _query: String? = null
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.apply {
            rvSearch.setHasFixedSize(true)
            rvSearch.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            movie.setOnClickListener {
                isMovie = true
                searchViewModel.isMovie = isMovie
            }
            tv.setOnClickListener {
                isMovie = false
                searchViewModel.isMovie = isMovie
            }
            searchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (p0 != null) {
                        searchViewModel.search(isMovie, p0)
                    }
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }
        searchViewModel.movieList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    response.data?.let { setMovieData(it) }
                }
                is Response.Error -> {}
                is Response.Loading -> {}
            }
        }
        searchViewModel.tvList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    response.data?.let { setTvData(it) }
                }
                is Response.Error -> {}
                is Response.Loading -> {}
            }
        }
    }

    private fun setMovieData(it: PopularList) {
        searchAdapterMovie = SearchAdapterMovie(it, onPosterClick = { openMediaDetailsBSD(it) })
        binding.rvSearch.adapter = searchAdapterMovie
    }

    private fun setTvData(it: Anime) {
        searchAdapter = SearchAdapter(it, onPosterClick = { openAnimeDetailsBSD(it) })
        binding.rvSearch.adapter = searchAdapter
    }

    private fun openMediaDetailsBSD(it: ResultX) {
        val action: NavDirections =
            SearchFragmentDirections.actionSearchFragmentToDetailFragment(it, null)
        findNavController().navigate(action)

    }

    private fun openAnimeDetailsBSD(it: AnimeDetail) {
        val action: NavDirections =
            SearchFragmentDirections.actionSearchFragmentToDetailFragment(null, it)
        findNavController().navigate(action)

    }
}