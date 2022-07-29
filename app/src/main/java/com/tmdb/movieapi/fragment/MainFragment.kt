package com.tmdb.movieapi.fragment

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.tmdb.movieapi.R
import com.tmdb.movieapi.adapter.PagingStateAdapter
import com.tmdb.movieapi.databinding.FragmentMainBinding
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.models.PopularList
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.paging.PagingAdapter
import com.tmdb.movieapi.paging.PagingAdapterAnime
import com.tmdb.movieapi.paging.PagingAdapterTvShow
import com.tmdb.movieapi.utils.Constants
import com.tmdb.movieapi.utils.Response
import com.tmdb.movieapi.viewmodels.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val quoteviewModel: QuoteViewModel by viewModels()
    private val binding get() = _binding!!
    val imageList = ArrayList<SlideModel>()
    private lateinit var popingAnim: Animation
    private lateinit var navController: NavController
    lateinit var pagingStateAdapter: PagingStateAdapter
    lateinit var pagingAdapter: PagingAdapter
    lateinit var pagingAdapterTvShow: PagingAdapterTvShow
    lateinit var pagingAdapterAnime: PagingAdapterAnime
    override fun onStart() {
        super.onStart()
        binding.imageSlider.startSliding()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.imageSlider.stopSliding()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        popingAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.poping_anim)
        pagingAdapter = PagingAdapter(onPosterClick = { openMediaDetailsBSD(it) })
        pagingAdapterAnime = PagingAdapterAnime(onPosterClick = { openAnimeDetailsBSD(it) })
        pagingAdapterTvShow = PagingAdapterTvShow(onTvClick = { openAnimeDetailsBSD(it) })
        setRecyclerView()
        binding.searchView.setOnClickListener { openSearch() }

        if (checkForInternet(requireContext())) {

        } else {
            onSNACK(requireView())
            binding.lottieLayout.isVisible = true
            binding.retry.setOnClickListener {
                quoteviewModel.retry()
                pagingAdapter.retry()
                pagingAdapterAnime.retry()
                pagingAdapterTvShow.retry()
            }
        }
        quoteviewModel.liveMovies.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> { response.data?.let { setLiveData(it) } }
                is Response.Error -> { }
                is Response.Loading -> {}
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedSv.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                val color = changeAppBarAlpha(
                    ContextCompat.getColor(requireContext(), R.color.black_transparent_1),
                    (min(255, scrollY).toFloat() / 255.0f).toDouble()
                )
                binding.appBarLayout.setBackgroundColor(color)
            }
        }
        quoteviewModel.pagingList.observe(viewLifecycleOwner) { pagingAdapter.submitData(lifecycle, it) }
        quoteviewModel.pagingTvList.observe(viewLifecycleOwner) { pagingAdapterTvShow.submitData(lifecycle, it) }
        quoteviewModel.pagingAnimeList.observe(viewLifecycleOwner) { pagingAdapterAnime.submitData(lifecycle, it) }
    }

    private fun setRecyclerView() {
        binding.animeRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.animeRv.adapter = pagingAdapterAnime.withLoadStateHeaderAndFooter(
            header = PagingStateAdapter { pagingAdapterAnime.retry() },
            footer = PagingStateAdapter { pagingAdapterAnime.retry() }
        )
        binding.tvRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.tvRv.adapter = pagingAdapterTvShow.withLoadStateHeaderAndFooter(
            header = PagingStateAdapter { pagingAdapterTvShow.retry() },
            footer = PagingStateAdapter { pagingAdapterTvShow.retry() }
        )
        binding.popularRv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.popularRv.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateAdapter { pagingAdapter.retry() },
            footer = PagingStateAdapter { pagingAdapter.retry() }
        )
    }

    private fun setLiveData(it: PopularList) {
        binding.lottieLayout.isVisible = false
        for (item in it.results) {
            imageList.add(
                SlideModel(
                    Constants.TMDB_IMAGE_BASE_URL_W780 + item.backdrop_path,
                    item.title
                )
            )
        }
        binding.imageSlider.setImageList(imageList)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                binding.imageSlider.stopSliding()
                openMediaDetailsBSD(it.results[position])
                binding.imageSlider.startSliding(3000)
            }
        })
    }

    private fun openMediaDetailsBSD(it: ResultX) {
        val action: NavDirections =
            MainFragmentDirections.actionMainFragmentToDetailFragment(it, null)
        findNavController().navigate(action)

    }

    private fun openAnimeDetailsBSD(it: AnimeDetail) {
        val action: NavDirections =
            MainFragmentDirections.actionMainFragmentToDetailFragment(null, it)
        findNavController().navigate(action)

    }

    private fun openSearch() {
        val action: NavDirections =
            MainFragmentDirections.actionMainFragmentToSearchFragment2()
        findNavController().navigate(action)
    }

    private fun changeAppBarAlpha(color: Int, fraction: Double): Int {
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val alpha: Int = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun onSNACK(view: View) {
        val snackbar = Snackbar.make(
            view, "Please Check Internet",
            Snackbar.LENGTH_LONG
        )
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.setAction("Retry") {
            quoteviewModel.retry()
        }
        snackbar.show()
    }
}

