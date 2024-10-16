package com.example.epfcinit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.epfcinit.databinding.ActivityEnterDetailsBinding

class EnterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnterDetailsBinding
    val epfcListAdapter = EPFCListAdapter(DataList.dataList)
//    val updateRecyclerView = MainActivity()
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEnterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.details)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


//        DataList.dataList


        binding.datePick.setOnClickListener {
            showDatePickerDialog(binding.datePick)
        }

        binding.submitDetails.setOnClickListener {
            val epfcData = EPFCData(binding.custName.text.toString(),
                binding.custPhone.text.toString(),
                binding.datePick.text.toString(),
                binding.kwh.text.toString(),
                binding.lag.text.toString(),
                binding.lead.text.toString())
            DataList.dataList.add(epfcData)
//            epfcListAdapter.notifyDataSetChanged()
            setResult(RESULT_OK)
            Toast.makeText(this, "${epfcData.kwh.toString()}", Toast.LENGTH_SHORT).show()

//            epfcListAdapter.update(DataList.dataList)
            finish()
        }

    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(selectedDate)
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
            }, year, month, day)

        datePickerDialog.show()
    }
}