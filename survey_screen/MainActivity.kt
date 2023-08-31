package com.example.justquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.justquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentQuestionIndex = 0
    private var questionsWithOptions: List<Triple<String, List<String>, Map<String, Int>>> = emptyList()
    private val answers: MutableMap<String, String> = mutableMapOf()
    private val selectedOptions: MutableMap<String, String?> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the spinner
        val countryOptions = resources.getStringArray(R.array.country_options)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = spinnerAdapter

        // Handle spinner item selection
        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = countryOptions[position]
                setQuestionsForCountry(selectedCountry)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setQuestionsForCountry(countryOptions[0]) // Set the initial set of questions

        binding.left.setOnClickListener {
            currentQuestionIndex--
            if (currentQuestionIndex < 0) {
                currentQuestionIndex = 0
            }
            setQuestionAndOptions(currentQuestionIndex)
        }

        binding.right.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex == questionsWithOptions.size) {
                currentQuestionIndex--
            }
            setQuestionAndOptions(currentQuestionIndex)
        }

        // Set onClickListener for each RadioButton to update the selected option
        binding.optionA.setOnClickListener { updateSelectedOption(it as RadioButton) }
        binding.optionB.setOnClickListener { updateSelectedOption(it as RadioButton) }
        binding.optionC.setOnClickListener { updateSelectedOption(it as RadioButton) }
        binding.optionD.setOnClickListener { updateSelectedOption(it as RadioButton) }

        // Set onClickListener for the Submit button
        binding.submitButton.setOnClickListener {
            if (currentQuestionIndex < questionsWithOptions.size - 1) {
                // If not on the last question, proceed to the next question
                currentQuestionIndex++
                setQuestionAndOptions(currentQuestionIndex)
            } else {
                // If on the last question, navigate to the submit page
                val selectedCountry = countryOptions[binding.spinnerCountry.selectedItemPosition]
                navigateToSubmitPage(selectedCountry)
            }
        }
    }

        private fun setQuestionsForCountry(country: String) {
        // Update the list of questionsWithOptions based on the selected country
        questionsWithOptions = when (country) {
            "India" -> listOf(
                Triple(
                    "Q1. This is Question 1 for India",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 1, // Assigning marks to each option
                        "Option B1" to 2,
                        "Option C1" to 3,
                        "Option D1" to 4
                    )
                ),
                Triple(
                    "Q2. This is Question 2 for India",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 5, // Assigning marks to each option
                        "Option B1" to 6,
                        "Option C1" to 7,
                        "Option D1" to 8
                    )
                ),

                // Add more questions for India
            )
            "Afghanistan" -> listOf(
                Triple(
                    "Q1. This is Question 1 for Afghanistan",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 9, // Assigning marks to each option
                        "Option B1" to 10,
                        "Option C1" to 11,
                        "Option D1" to 12
                    )
                ),
                Triple(
                    "Q2. This is Question 2 for Afghanistan",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 13, // Assigning marks to each option
                        "Option B1" to 14,
                        "Option C1" to 15,
                        "Option D1" to 16
                    )
                ),
                // Add more questions for Afghanistan
            )
            "Nepal" -> listOf(
                Triple(
                    "Q1. This is Question 1 for Nepal",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 17, // Assigning marks to each option
                        "Option B1" to 18,
                        "Option C1" to 19,
                        "Option D1" to 20
                    )
                ),
                Triple(
                    "Q2. This is Question 2 for Nepal",
                    listOf("Option A1", "Option B1", "Option C1", "Option D1"),
                    mapOf(
                        "Option A1" to 21, // Assigning marks to each option
                        "Option B1" to 22,
                        "Option C1" to 23,
                        "Option D1" to 24
                    )
                ),
                // Add more questions for Nepal
            )
            else -> emptyList()
        }

        // Reset the currentQuestionIndex and update the questions
        currentQuestionIndex = 0
        setQuestionAndOptions(currentQuestionIndex)
    }

    private fun setQuestionAndOptions(index: Int) {
        val (question, options, optionMarks) = questionsWithOptions[index]
        binding.tvQuestion.text = question
        binding.optionA.text = "${options[0]} (${optionMarks[options[0]]} marks)"
        binding.optionB.text = "${options[1]} (${optionMarks[options[1]]} marks)"
        binding.optionC.text = "${options[2]} (${optionMarks[options[2]]} marks)"
        binding.optionD.text = "${options[3]} (${optionMarks[options[3]]} marks)"


        // Check if a selection was made for this question
        if (selectedOptions.containsKey(question)) {
            // If a selection was made, find the corresponding RadioButton and set it as checked
            val selectedOption = selectedOptions[question]
            when (selectedOption) {
                options[0] -> binding.optionA.isChecked = true
                options[1] -> binding.optionB.isChecked = true
                options[2] -> binding.optionC.isChecked = true
                options[3] -> binding.optionD.isChecked = true
            }
        } else {
            // If no selection was made, clear the checked state of the RadioGroup
            binding.optionsRadioGroup.clearCheck()
        }
    }

    private fun updateSelectedOption(radioButton: RadioButton) {
        val question = questionsWithOptions[currentQuestionIndex].first
        selectedOptions[question] = radioButton.text.toString()
        val selectedMarks = questionsWithOptions[currentQuestionIndex].third[radioButton.text.toString()]
        Log.d("SelectedOption", "Question ${currentQuestionIndex + 1}: ${selectedOptions[question]} ($selectedMarks marks)")

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