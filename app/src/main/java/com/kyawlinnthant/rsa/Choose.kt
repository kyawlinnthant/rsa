package com.kyawlinnthant.rsa

import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.layout_choose.*

class Choose : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.layout_choose

    override fun setupView() {

        sender.setOnClickListener { findNavController().navigate(R.id.nav_sender) }
        receiver.setOnClickListener { findNavController().navigate(R.id.nav_receiver) }
    }

    override fun listen() {

    }
}