package com.example.eventmanager.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.eventmanager.R
import com.example.eventmanager.databinding.FragmentEventItemBinding
import com.example.eventmanager.domain.EventItem
import com.example.eventmanager.presentation.MainFragment.Companion.EVENT_ITEM_ID
import com.example.eventmanager.presentation.MainFragment.Companion.MODE_ADD
import com.example.eventmanager.presentation.MainFragment.Companion.MODE_EDIT
import com.example.eventmanager.presentation.MainFragment.Companion.SCREEN_MODE
import javax.inject.Inject


class EventItemFragment : Fragment() {

    private lateinit var viewModel: EventItemViewModel

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
            Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
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
        if (binding.etCity.text?.isNotBlank() == true) {
            viewModel.resetErrorInputCity()
        }
    }

    private fun launchEditMode() {
        viewModel.getEventItem(eventItemId)
        binding.tilCity.hint = context?.getString(R.string.current_city)
        binding.hintSpinner.text = context?.getString(R.string.new_city)
        binding.citiesSpinner.setSelection(0)

        binding.saveButton.setOnClickListener {
            val city = binding.citiesSpinner.selectedItem.toString().ifBlank {
                binding.etCity.text?.toString()
            }

            viewModel.editEventItem(
                binding.etName.text?.toString(),
                binding.etDescription.text?.toString(),
                binding.etDate.text?.toString(),
                city
            )
        }
    }

    private fun launchAddMode() {
        binding.tilCity.visibility = View.GONE
        binding.hintSpinner.text = context?.getString(R.string.choose_city)
        binding.citiesSpinner.setSelection(1)

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

    companion object {

        private const val MODE_UNKNOWN = ""
    }
}
