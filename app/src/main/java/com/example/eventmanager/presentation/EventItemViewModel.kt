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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    private val _errorInputCity = MutableLiveData<Boolean>()
    val errorInputCity: LiveData<Boolean>
        get() = _errorInputCity

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
        inputCity: String?
    ) {
        val name = parseName(inputName)
        val description = parseDescription(inputDescription)
        val date = parseDate(inputDate)
        val city = parseCity(inputCity)
        val fieldsValid = validateInput(name, date, city)
        if (fieldsValid) {
            viewModelScope.launch {
                val listTemperature =
                    getTemperatureUseCase.getTemperatureData(city, date, addOneDay(date))
                val temperature =
                    if (listTemperature.isNotEmpty()) listTemperature[0].temp else 0.0
                val eventItem = EventItem(
                    name,
                    description,
                    date,
                    city,
                    visited = false,
                    missed = false,
                    temperature.toString(),
                    "https://i.imgur.com/DvpvklR.png"
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
        inputCity: String?
    ) {
        val name = parseName(inputName)
        val description = parseDescription(inputDescription)
        val date = parseDate(inputDate)
        val city = parseCity(inputCity)
        val fieldsValid = validateInput(name, date, city)
        if (fieldsValid) {
            _eventItem.value?.let {
                viewModelScope.launch {
                    val listTemperature =
                        getTemperatureUseCase.getTemperatureData(city, date, addOneDay(date))
                    val temperature =
                        if (listTemperature.isNotEmpty()) listTemperature[0].temp.toString() else 0.0
                    val eventItem = it.copy(
                        name = name,
                        description = description,
                        date = date,
                        city = city,
                        temperature = temperature.toString()
                    )
                    editEventItemUseCase.editEventItem(eventItem)
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

    private fun parseCity(inputCity: String?): String {
        return inputCity?.trim() ?: ""
    }

    private fun addOneDay(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(date, formatter)
        val nextDay = parsedDate.plusDays(1)
        return nextDay.format(formatter)
    }

    private fun validateInput(name: String, date: String, city: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (date.isBlank() || !isValidDateFormat(date)) {
            _errorInputDate.value = true
            result = false
        }
/*        if (city.isBlank()) { //TODO добавить проверку адреса
            _errorInputCity.value = true
            result = false
        }*/
        return result
    }


    private fun isValidDateFormat(date: String): Boolean {
        val regex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\$")
        return regex.matches(date)
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputDate() {
        _errorInputDate.value = false
    }

    fun resetErrorInputCity() {
        _errorInputCity.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}
