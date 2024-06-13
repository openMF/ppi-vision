package com.example.survey_screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.survey_screen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentQuestionIndex = 0
    private var questionsWithOptions: List<Pair<String, List<String>>> = emptyList()
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
            "Ghana" -> listOf(
                "Q1. HOW MANY MEMBERS ARE THERE IN THE HOUSEHOLD?" to listOf("1-5", "5-10", "10-15", "15-20"),
                "Q2. IN THE PAST MONTH, HAVE YOU PURCHASED ANY CHICKEN EGGS (FRESH OR SINGLE)?" to listOf("Yes", "No"),
                "Q3. IN THE PAST MONTH, HAVE YOU PURCHASED ANY RAW OR CORNED BEEF?" to listOf("Yes", "No"),
                "Q4. WHAT IS THE MAIN CONSTRUCTION MATERIAL USED FOR THE OUTER WALL?" to listOf("Stones", "Earth-based bricks","Sand-cement","Other"),
                "Q5. WHAT IS THE MAIN FUEL USED BY THE HOUSEHOLD FOR COOKING?" to listOf("Wood", "Charcoal", "Crop residue", "Gas"),
            )
            "Malawi" -> listOf(
                "Q1. HOW MANY HOUSEHOLD MEMBERS LIVE IN THE HOUSEHOLD?" to listOf("1-5", "5-10", "10-15", "15-20"),
                "Q2. IS THE HOUSEHOLD HEAD ABLE TO READ AND WRITE IN ENGLISH?" to listOf("Yes", "No"),
                "Q3. THE ROOF OF THE MAIN DWELLING IS PREDOMINANTLY MADE OF WHAT MATERIAL?" to listOf("Stones", "Earth-based bricks","Sand-cement","Other"),
                "Q4. THE FLOOR OF THE MAIN DWELLING IS PREDOMINANTLY MADE OF WHAT MATERIAL?" to listOf("Stones", "Earth-based bricks","Sand-cement","Other"),
                "Q5. WHAT IS THE MAIN SOURCE OF COOKING FUEL IN YOUR HOUSEHOLD?" to listOf("Wood", "Charcoal", "Crop residue", "Gas"),
                "Q6. OVER THE PAST ONE WEEK (7 DAYS), DID YOU OR OTHERS IN YOUR HOUSEHOLD CONSUME ANY SUGAR?" to listOf("Yes", "No"),
            )
            "Burkina Faso" -> listOf(
                "Q1. HOW MANY MEMBERS ARE THERE IN THE HOUSEHOLD?" to listOf("1-5", "5-10", "10-15", "15-20"),
                "Q2. DOES THE MALE HOUSEHOLD HEAD OR SPOUSE READ AND WRITE IN ANY LANGUAGE?" to listOf("Yes", "No"),
                "Q3. DID EVERY CHILD AGED 7 TO 14 ATTEND FORMAL SCHOOL DURING THE LAST SCHOOL YEAR?" to listOf("Yes", "No"),
                "Q4. WHAT IS THE MATERIAL USED TO CONSTRUCT THE FLOOR?" to listOf("Stones", "Earth-based bricks","Sand-cement","Other"),
                "Q5. HAS YOUR HOUSEHOLD CONSUMED MILK AND/OR DAIRY PRODUCTS IN THE LAST DAYS?" to listOf("Yes", "No"),
                "Q6. HAS YOUR HOUSEHOLD CONSUMED SUGAR (GRANULATED OR IN THE FORM OF CUBES) IN THE LAST 7 DAYS??" to listOf("Yes", "No"),
                "Q7. WHAT IS THE MAIN SOURCE OF HOUSEHOLD LIGHTING?" to listOf("Firewood", "Charcoal", "Kerosene", "Animal dung"),
            )
            else -> emptyList()
        }

        // Reset the currentQuestionIndex and update the questions
        currentQuestionIndex = 0
        setQuestionAndOptions(currentQuestionIndex)
    }

    private fun setQuestionAndOptions(index: Int) {
        val (question, options) = questionsWithOptions[index]
        binding.tvQuestion.text = question
        binding.optionA.text = options[0]
        binding.optionB.text = options[1]
        binding.optionC.text = options[2]
        binding.optionD.text = options[3]

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
