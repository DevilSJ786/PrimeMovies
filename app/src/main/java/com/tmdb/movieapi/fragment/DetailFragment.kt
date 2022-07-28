package com.tmdb.movieapi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tmdb.movieapi.databinding.FragmentDetailBinding
import com.tmdb.movieapi.models.AnimeDetail
import com.tmdb.movieapi.models.ResultX
import com.tmdb.movieapi.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var popingAnim: Animation
    private lateinit var navController: NavController
    private var isExpanded: Boolean = false
    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        args.resultx?.let {
            val go = it
            binding.apply {
                posterImage.load(Constants.TMDB_IMAGE_BASE_URL_W500.plus(it.backdrop_path))
                overviewText.text = it.overview
                closeDetailBtn.setOnClickListener { dismiss() }
                titleText.text = it.title
                ratingText.text = it.vote_average.toString()
                releaseDate.text = it.release_date
                playButton.setOnClickListener { goToPlayer(go,null) }
                overviewText.setOnClickListener {
                    if (isExpanded) {
                        overviewText.maxLines = 6
                        isExpanded = true
                    } else {
                        isExpanded = false
                        overviewText.maxLines = 4
                    }
                }
            }
        }
        args.anime?.let {
            val data = it
            binding.apply {
                posterImage.load(Constants.TMDB_IMAGE_BASE_URL_W500.plus(it.backdrop_path))
                overviewText.text = it.overview
                closeDetailBtn.setOnClickListener { dismiss() }
                titleText.text = it.name
                ratingText.text = it.vote_average.toString()
                releaseDate.text = it.first_air_date
                playButton.setOnClickListener { goToPlayer(null,data) }
                overviewText.setOnClickListener {
                    if (isExpanded) {
                        overviewText.maxLines = 6
                        isExpanded = true
                    } else {
                        isExpanded = false
                        overviewText.maxLines = 4
                    }
                }
            }
        }

        navController = findNavController()
    }

    private fun goToPlayer(result: ResultX?, result1: AnimeDetail?) {
        result?.let {
            val action: NavDirections =
                DetailFragmentDirections.actionDetailFragmentToFullDetailFragment22(it,null)
            findNavController().navigate(action)
        }
        result1?.let {
            val action: NavDirections =
                DetailFragmentDirections.actionDetailFragmentToFullDetailFragment22(null,it)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}