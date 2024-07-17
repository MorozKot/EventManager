package com.example.eventmanager.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanager.R
import com.example.eventmanager.databinding.FragmentMainBinding
import javax.inject.Inject

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var eventListAdapter: EventListAdapter
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as EventApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.eventList.observe(viewLifecycleOwner) {
            eventListAdapter.submitList(it)
        }
        binding.buttonAddEventItem.setOnClickListener {
            val bundle = bundleOf(SCREEN_MODE to MODE_ADD)
            findNavController().navigate(R.id.action_mainFragment_to_eventItemFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvEventList) {
            eventListAdapter = EventListAdapter()
            adapter = eventListAdapter
            recycledViewPool.setMaxRecycledViews(
                EventListAdapter.VIEW_TYPE_UPCOMING,
                EventListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                EventListAdapter.VIEW_TYPE_VISITED,
                EventListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                EventListAdapter.VIEW_TYPE_MISSED,
                EventListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(binding.rvEventList)
    }

    private fun setupSwipeListener(rvEventList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = eventListAdapter.currentList[viewHolder.getBindingAdapterPosition()]
                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteEventItem(item)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.changeMissedState(item)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvEventList)
    }

    private fun setupClickListener() {

        eventListAdapter.onEventItemClickListener = { eventItem ->
            val bundle = bundleOf(
                SCREEN_MODE to MODE_EDIT,
                EVENT_ITEM_ID to eventItem.id
            )
            findNavController().navigate(
                R.id.action_mainFragment_to_eventItemFragment,
                bundle
            )
        }
    }

    private fun setupLongClickListener() {
        eventListAdapter.onEventItemLongClickListener = {
            viewModel.changeVisitedState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val SCREEN_MODE = "extra_mode"
        const val EVENT_ITEM_ID = "extra_event_item_id"
        const val MODE_EDIT = "mode_edit"
        const val MODE_ADD = "mode_add"
    }
}