package org.mifos.visionppi.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.mifos.visionppi.MainActivity
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivitySurveyBinding
import java.io.IOException
import java.util.*

class SurveyActivity : AppCompatActivity() {

    private lateinit var activitySurveyBinding: ActivitySurveyBinding
    private var currentQuestionIndex = 0
    private var questionsWithOptions: List<Pair<String, List<String>>> = emptyList()
    private val answers: MutableMap<String, String> = mutableMapOf()
    private val selectedOptions: MutableMap<String, String?> = mutableMapOf()
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySurveyBinding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(activitySurveyBinding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        var country:String?=null
        val countryOptions = resources.getStringArray(R.array.country_options)

        if (isLocationPermissionGranted()) {
            lifecycleScope.launch(Dispatchers.Main) {

                country = getLastKnownLocation()

                // Set up the spinner
                val spinnerAdapter = ArrayAdapter(this@SurveyActivity, android.R.layout.simple_spinner_item, countryOptions)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                activitySurveyBinding.spinnerCountry.adapter = spinnerAdapter

                if (country!=null){
                    val position = countryOptions.indexOf(country)
                    if (position != -1) {
                        activitySurveyBinding.spinnerCountry.setSelection(position)
                    }
                }
            }
        }

        // Handle spinner item selection
        activitySurveyBinding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = countryOptions[position]
                setQuestionsForCountry(selectedCountry)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setQuestionsForCountry(countryOptions[0]) // Set the initial set of questions

        activitySurveyBinding.left.setOnClickListener {
            currentQuestionIndex--
            if (currentQuestionIndex < 0) {
                currentQuestionIndex = 0
            }
            setQuestionAndOptions(currentQuestionIndex)
        }

        activitySurveyBinding.right.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex == questionsWithOptions.size) {
                currentQuestionIndex--
            }
            setQuestionAndOptions(currentQuestionIndex)
        }

        // Set onClickListener for each RadioButton to update the selected option
        activitySurveyBinding.optionA.setOnClickListener { updateSelectedOption(it as RadioButton) }
        activitySurveyBinding.optionB.setOnClickListener { updateSelectedOption(it as RadioButton) }
        activitySurveyBinding.optionC.setOnClickListener { updateSelectedOption(it as RadioButton) }
        activitySurveyBinding.optionD.setOnClickListener { updateSelectedOption(it as RadioButton) }

        // Set onClickListener for the Submit button
        activitySurveyBinding.submitButton.setOnClickListener {
            if (currentQuestionIndex < questionsWithOptions.size - 1) {
                // If not on the last question, proceed to the next question
                currentQuestionIndex++
                setQuestionAndOptions(currentQuestionIndex)
            } else {
                // If on the last question, navigate to the submit page
                val selectedCountry = countryOptions[activitySurveyBinding.spinnerCountry.selectedItemPosition]
                navigateToSubmitPage(selectedCountry)
            }
        }
    }

    private fun setQuestionsForCountry(country: String) {
        // Update the list of questionsWithOptions based on the selected country
        questionsWithOptions = when (country) {
            "India" -> listOf(
                    "Q1. This is Question 1 for India" to listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    "Q2. This is 2nd one for India" to listOf("Option A2", "Option B2", "Option C2", "Option D2"),
                    // Add more questions for India
            )
            "Afghanistan" -> listOf(
                    "Q1. This is Question 1 for Afghanistan" to listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    "Q2. This is 2nd one for Afghanistan" to listOf("Option A2", "Option B2", "Option C2", "Option D2"),
                    // Add more questions for Afghanistan
            )
            "Nepal" -> listOf(
                    "Q1. This is Question 1 for Nepal" to listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    "Q2. This is 2nd one for Nepal" to listOf("Option A2", "Option B2", "Option C2", "Option D2"),
                    // Add more questions for Nepal
            )
            else -> emptyList()
        }

        // Reset the currentQuestionIndex and update the questions
        currentQuestionIndex = 0
        setQuestionAndOptions(currentQuestionIndex)
    }

    private fun setQuestionAndOptions(index: Int) {
        val (question, options) = questionsWithOptions[index]
        activitySurveyBinding.tvQuestion.text = question
        activitySurveyBinding.optionA.text = options[0]
        activitySurveyBinding.optionB.text = options[1]
        activitySurveyBinding.optionC.text = options[2]
        activitySurveyBinding.optionD.text = options[3]

        // Check if a selection was made for this question
        if (selectedOptions.containsKey(question)) {
            // If a selection was made, find the corresponding RadioButton and set it as checked
            val selectedOption = selectedOptions[question]
            when (selectedOption) {
                options[0] -> activitySurveyBinding.optionA.isChecked = true
                options[1] -> activitySurveyBinding.optionB.isChecked = true
                options[2] -> activitySurveyBinding.optionC.isChecked = true
                options[3] -> activitySurveyBinding.optionD.isChecked = true
            }
        } else {
            // If no selection was made, clear the checked state of the RadioGroup
            activitySurveyBinding.optionsRadioGroup.clearCheck()
        }
    }

    private fun updateSelectedOption(radioButton: RadioButton) {
        val question = questionsWithOptions[currentQuestionIndex].first
        selectedOptions[question] = radioButton.text.toString()
        Log.d("SelectedOption", "Question ${currentQuestionIndex + 1}: ${selectedOptions[question]}")
        // Add the selected answer to the dictionary
        answers["Question ${currentQuestionIndex + 1}"] = selectedOptions[question] ?: "Not answered"
    }

    private fun navigateToSubmitPage(selectedCountry: String) {
        // Inflate the submit page layout and display it
        val submitLayout = layoutInflater.inflate(R.layout.activity_submit, null)
        val tvSelectedCountry = submitLayout.findViewById<TextView>(R.id.tvSelectedCountry)
        val tvAnswers = submitLayout.findViewById<TextView>(R.id.tvAnswers)
        val btnSubmit = submitLayout.findViewById<TextView>(R.id.btnSubmit)

        // Set the selected country name
        tvSelectedCountry.text = "Selected Country: $selectedCountry"
        Log.d("FinalAnswers", "Selected Country: $selectedCountry")

        // Build the answers text to display and print in the log
        val allAnswers = StringBuilder()
        for ((question, answer) in answers) {
            val formattedAnswer = answer ?: "No answer"
            allAnswers.append("$question: $formattedAnswer\n")

            Log.d("FinalAnswers", "$question: $formattedAnswer")
        }
        tvAnswers.text = allAnswers.toString()

        // Create a Dialog to show the submit page
        val dialog = AlertDialog.Builder(this)
                .setView(submitLayout)
                .setCancelable(false)
                .create()

        // Handle the submit button click
        btnSubmit.setOnClickListener {
            // Clear the answers dictionary, selectedOptions map, and return to the first question
            answers.clear()
            selectedOptions.clear()
            currentQuestionIndex = 0
            setQuestionAndOptions(currentQuestionIndex)
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Show the Dialog
        dialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissions Not Granted", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    private suspend fun getLastKnownLocation(): String? {
        var countryName: String? = null

        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissions Not Granted", Toast.LENGTH_SHORT).show()
            return countryName
        }

        try {
            val location = fusedLocationProviderClient.lastLocation.await()
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                val coordinates = "Latitude: $latitude, Longitude: $longitude"
                countryName = getCountryName(latitude, longitude)
                if (countryName == null) {
                    Toast.makeText(this, "Country name not found", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show()
            }
        } catch (exception: Exception) {
            Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show()
        }

        return countryName
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lifecycleScope.launch(Dispatchers.Main) {
                    val country = getLastKnownLocation()
                }
            } else {
                Toast.makeText(this, "Permissions Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCountryName(latitude: Double, longitude: Double): String? {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].countryName
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }

}
