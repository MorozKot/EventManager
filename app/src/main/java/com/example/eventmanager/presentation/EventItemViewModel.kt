package com.example.eventmanager.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmanager.domain.AddEventItemUseCase
import com.example.eventmanager.domain.EditEventItemUseCase
import com.example.eventmanager.domain.EventItem
import com.example.eventmanager.domain.GetEventItemUseCase
import com.example.eventmanager.domain.GetTemperatureUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventItemViewModel @Inject constructor(
    private val getEventItemUseCase: GetEventItemUseCase,
    private val addEventItemUseCase: AddEventItemUseCase,
    private val editEventItemUseCase: EditEventItemUseCase,
    private val getTemperatureUseCase: GetTemperatureUseCase
) : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputDate = MutableLiveData<Boolean>()
    val errorInputDate: LiveData<Boolean>
        get() = _errorInputDate

    private val _errorInputAddress = MutableLiveData<Boolean>()
    val errorInputAddress: LiveData<Boolean>
        get() = _errorInputAddress

    private val _eventItem = MutableLiveData<EventItem>()
    val eventItem: LiveData<EventItem>
        get() = _eventItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getEventItem(eventItemId: Int) {
        viewModelScope.launch {
            val item = getEventItemUseCase.getEventItem(eventItemId)
            _eventItem.value = item
        }
    }

    fun addEventItem(
        inputName: String?,
        inputDescription: String?,
        inputDate: String?,
        inputAddress: String?
    ) {
        val name = parseName(inputName)
        val description = parseDescription(inputDescription)
        val date = parseDate(inputDate)
        val address = parseAddress(inputAddress)
        val fieldsValid = validateInput(name, date, address)
        if (fieldsValid) {
            viewModelScope.launch {
                val listTemperature = getTemperatureUseCase.getTemperatureData("Aga,PH","2023-07-18", "2023-07-19")
                val temperature = listTemperature[0].temp
                val eventItem = EventItem(name, description, date, address, "", false,false,
                    temperature.toString()
                )
                addEventItemUseCase.addEventItem(eventItem)
                finishWork()
            }
        }
    }

    fun editEventItem(
        inputName: String?,
        inputDescription: String?,
        inputDate: String?,
        inputAddress: String?
    ) {
        val name = parseName(inputName)
        val description = parseDescription(inputDescription)
        val date = parseDate(inputDate)
        val address = parseAddress(inputAddress)
        val fieldsValid = validateInput(name, date, address)
        if (fieldsValid) {
            _eventItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(
                        name = name,
                        description = description,
                        date = date,
                        address = address
                    )
                    editEventItemUseCase.editEventItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseDescription(inputDescription: String?): String {
        return inputDescription?.trim() ?: ""
    }

    private fun parseDate(inputDate: String?): String {
        return inputDate?.trim() ?: ""
    }

    private fun parseAddress(inputAddress: String?): String {
        return inputAddress?.trim() ?: ""
    }

    private fun validateInput(name: String, date: String, address: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (date.isBlank() || !isValidDateFormat(date)) { //TODO добавить проверку формата даты
            _errorInputDate.value = true
            result = false
        }
        if (address.isBlank()) { //TODO добавить проверку адреса
            _errorInputAddress.value = true
            result = false
        }
        return result
    }


    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\$")
        println("isValidDateFormat ${regex.matches(date)}")
        return regex.matches(date)
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputDate() {
        _errorInputDate.value = false
    }

    fun resetErrorInputAddress() {
        _errorInputAddress.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}
