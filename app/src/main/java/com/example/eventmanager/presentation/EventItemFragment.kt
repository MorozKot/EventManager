package com.example.eventmanager.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eventmanager.databinding.FragmentEventItemBinding
import com.example.eventmanager.domain.EventItem
import javax.inject.Inject


class EventItemFragment : Fragment() {

    private lateinit var viewModel: EventItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentEventItemBinding? = null
    private val binding: FragmentEventItemBinding
        get() = _binding ?: throw RuntimeException("FragmentEventItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var eventItemId: Int = EventItem.UNDEFINED_ID

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as EventApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[EventItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputDate()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
/*        if (!binding.etCity.text.isNullOrEmpty()) {
            viewModel.resetErrorInputCity()
        }*/
/*        binding.etCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCity()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })*/
    }

    private fun launchEditMode() {
        viewModel.getEventItem(eventItemId)
        binding.tilCity.hint = "Текущее место проведения"
        binding.citiesSpinner.setSelection(0)
        binding.hintSpinner.text = "Выберите новое место проведения"

        binding.saveButton.setOnClickListener {
            println("citiesSpinner.getItemIdAtPosition1 ${binding.citiesSpinner.adapter.getItem(0).toString()}")
            println("citiesSpinner.selectedItem ${binding.citiesSpinner.selectedItem.toString()}")
            val newCity = if (binding.citiesSpinner.selectedItem.toString().isBlank()) {
                binding.etCity.text?.toString()
            } else {
                binding.citiesSpinner.selectedItem.toString()
            }

            viewModel.editEventItem(
                binding.etName.text?.toString(),
                binding.etDescription.text?.toString(),
                binding.etDate.text?.toString(),
                newCity
            )
        }
    }

    private fun launchAddMode() {
        binding.tilCity.visibility = View.GONE
        binding.citiesSpinner.setSelection(1)
        binding.hintSpinner.text = "Выберите место проведения"

        binding.saveButton.setOnClickListener {
            viewModel.addEventItem(
                binding.etName.text?.toString(),
                binding.etDescription.text?.toString(),
                binding.etDate.text?.toString(),
                binding.citiesSpinner.selectedItem.toString()
            )
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EVENT_ITEM_ID)) {
                throw RuntimeException("Param event item id is absent")
            }
            eventItemId = args.getInt(EVENT_ITEM_ID, EventItem.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val EVENT_ITEM_ID = "extra_event_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): EventItemFragment {
            return EventItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(eventItemId: Int): EventItemFragment {
            return EventItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(EVENT_ITEM_ID, eventItemId)
                }
            }
        }
    }
}
