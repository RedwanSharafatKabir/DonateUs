package com.donate.us.viewpagers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.donate.us.databinding.FragmentDonateHistoryBinding

class DonateHistory : Fragment() {

    private lateinit var binding: FragmentDonateHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDonateHistoryBinding.inflate(layoutInflater)

        return binding.root
    }
}
