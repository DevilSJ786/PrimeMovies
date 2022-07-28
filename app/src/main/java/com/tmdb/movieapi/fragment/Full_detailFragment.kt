package com.tmdb.movieapi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.tmdb.movieapi.adapter.CastAdapter
import com.tmdb.movieapi.databinding.FragmentFullDetailBinding
import com.tmdb.movieapi.models.CastList
import com.tmdb.movieapi.models.VideoList
import com.tmdb.movieapi.utils.Constants.TMDB_IMAGE_BASE_URL_W780
import com.tmdb.movieapi.utils.Response
import com.tmdb.movieapi.viewmodels.MovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Full_detailFragment : Fragment() {

    private var _binding: FragmentFullDetailBinding? = null
    private val binding get() = _binding!!
    var videoId: String = "0nfm4jR3Zzw"
    private var _mediaId: Int? = null
    private lateinit var adapter: CastAdapter
    private lateinit var navController: NavController
    private val args by navArgs<Full_detailFragmentArgs>()

    @Inject
    lateinit var factory: MovieViewModel.Factory

    private val viewModel: MovieViewModel by viewModels {
        MovieViewModel.providesFactory(
            assistedFactory = factory,
            id = if (args.resultx == null) args.anime!!.id else args.resultx!!.id,
            args.resultx == null
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        lifecycle.addObserver(binding.youtubePlayer);

        args.resultx?.let {
            binding.apply {
                posterIv.load(TMDB_IMAGE_BASE_URL_W780 + it.backdrop_path)
                titleName.text = it.title
                overviewText.text = it.overview
                releaseDate.text = it.release_date
            }
        }
        args.anime?.let {
            binding.apply {
                posterIv.load(TMDB_IMAGE_BASE_URL_W780 + it.backdrop_path)
                titleName.text = it.name
                overviewText.text = it.overview
                releaseDate.text = it.first_air_date
            }
        }

        viewModel.castList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    response.data?.let { setCastLiveData(it) }
                }
                is Response.Error -> {}
                is Response.Loading -> {}
            }
        }

        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                binding.posterIv.visibility = View.GONE
                Play(youTubePlayer)
            }
        })

    }

    private fun setCastLiveData(it: CastList) {
        adapter = CastAdapter(it.cast)
        binding.apply {
            rvCast.setHasFixedSize(true)
            rvCast.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvCast.adapter = adapter
        }
    }

    fun Play(youTubePlayer: YouTubePlayer) {
        viewModel.videolist.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    response.data?.let { setYoutubeData(it, youTubePlayer) }
                }
                is Response.Error -> {}
                is Response.Loading -> {}
            }
        }
    }

    private fun setYoutubeData(it: VideoList, youTubePlayer: YouTubePlayer) {
        if (it.results.isNotEmpty()) {
            videoId = it.results[0].key
            youTubePlayer.loadVideo(videoId, 0F)
        }
    }


}