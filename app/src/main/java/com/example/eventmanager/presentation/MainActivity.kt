package com.example.eventmanager.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanager.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), EventItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as EventApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.eventList.observe(this) {
            eventListAdapter.submitList(it)
        }
        binding.buttonAddEventItem.setOnClickListener {
                launchFragment(EventItemFragment.newInstanceAddItem())
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(fragment, "MY_TAG")
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        with(binding.rvEventList) {
            eventListAdapter = EventListAdapter()
            adapter = eventListAdapter
            recycledViewPool.setMaxRecycledViews(
                EventListAdapter.VIEW_TYPE_VISITED,
                EventListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                EventListAdapter.VIEW_TYPE_UPCOMING,
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
                val item = eventListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteEventItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvEventList)
    }

    private fun setupClickListener() {
        eventListAdapter.onEventItemClickListener = {
                launchFragment(EventItemFragment.newInstanceEditItem(it.id))
        }
    }

    private fun setupLongClickListener() {
        eventListAdapter.onEventItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
}