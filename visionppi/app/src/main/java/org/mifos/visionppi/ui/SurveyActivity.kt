package org.mifos.visionppi.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivitySurveyBinding

class SurveyActivity : AppCompatActivity() {

    private lateinit var activitySurveyBinding: ActivitySurveyBinding
    private var currentQuestionIndex = 0
    private var questionsWithOptions: List<Pair<String, List<String>>> = emptyList()
    private val answers: MutableMap<String, String> = mutableMapOf()
    private val selectedOptions: MutableMap<String, String?> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySurveyBinding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(activitySurveyBinding.root)

        // Set up the spinner
        val countryOptions = resources.getStringArray(R.array.country_options)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySurveyBinding.spinnerCountry.adapter = spinnerAdapter

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
        }

        // Show the Dialog
        dialog.show()
    }
}
