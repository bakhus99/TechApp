package com.student.techapp.adapter

import android.view.View
import com.student.techapp.R
import com.student.techapp.databinding.RequestItemBinding
import com.student.techapp.models.Request
import com.xwray.groupie.viewbinding.BindableItem

class ApplicationRequestItem(val request: Request) : BindableItem<RequestItemBinding>() {
    override fun bind(viewBinding: RequestItemBinding, position: Int) {
        viewBinding.tvAddress.text = request.address
        viewBinding.tvName.text = request.name
        viewBinding.tvService.text = request.service
        viewBinding.tvProblem.text = request.problem
    }

    override fun getLayout() = R.layout.request_item

    override fun initializeViewBinding(view: View): RequestItemBinding =
        RequestItemBinding.bind(view)


}