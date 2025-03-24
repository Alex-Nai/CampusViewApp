package com.example.campusview.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campusview.databinding.FragmentMyBookingsBinding
import com.example.campusview.ui.adapter.BookingAdapter
import com.example.campusview.ui.viewmodel.BookingViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyBookingsFragment : Fragment() {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingViewModel by viewModels()
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        
        // 首次进入页面加载数据
        viewModel.loadMyBookings(isRefresh = true)
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter { booking ->
            showCancelConfirmationDialog(booking.id)
        }
        
        binding.recyclerView.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            
            // 添加滚动监听，实现上拉加载更多
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!binding.loadingMore.isVisible &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        viewModel.loadMoreBookings()
                    }
                }
            })
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshBookings()
        }
    }

    private fun observeViewModel() {
        viewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            bookingAdapter.submitList(bookings)
            binding.emptyText.isVisible = bookings.isEmpty()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
            if (isLoading) {
                binding.progressBar.isVisible = true
                binding.loadingMore.isVisible = false
            } else {
                binding.progressBar.isVisible = false
            }
        }

        viewModel.isLoadingMore.observe(viewLifecycleOwner) { isLoadingMore ->
            binding.loadingMore.isVisible = isLoadingMore
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun showCancelConfirmationDialog(bookingId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("取消预约")
            .setMessage("确定要取消这个预约吗？")
            .setPositiveButton("确定") { _, _ ->
                viewModel.cancelBooking(bookingId)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("错误")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MyBookingsFragment()
    }
} 