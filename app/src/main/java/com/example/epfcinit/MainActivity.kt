package com.example.epfcinit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.epfcinit.databinding.ActivityMainBinding
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.IOException
import java.net.URI


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
        val FILE_SELECT_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.excelUploadBtn.setOnClickListener {
            val intent:Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("application/vnd.ms-excel")
            startActivityForResult(intent, FILE_SELECT_CODE)
        }
    }
    private fun readExcelFile(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val workbook = WorkbookFactory.create(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0) // Assuming you want the first sheet

            // Iterate through rows and cells
            val rowIterator: Iterator<Row> = sheet.iterator()
            while (rowIterator.hasNext()) {
                val row: Row = rowIterator.next()
                val cellIterator: Iterator<Cell> = row.cellIterator()
                while (cellIterator.hasNext()) {
                    val cell = cellIterator.next()
//                    when (cell.cellType) {
//                        CellType.STRING -> var cellValue: String = cell.stringCellValue
//                        CellType.NUMERIC ->var numericCellValue: Double = cell.numericCellValue
//                        else -> TODO()
//                    }
                    val cellValue: Any = when (cell.cellType) {
                        CellType.STRING -> cell.stringCellValue
                        CellType.NUMERIC -> cell.numericCellValue
                        else -> TODO()
                    }
                }
            }
            workbook.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidFormatException) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resData)
        if (requestCode == FILE_SELECT_CODE && resultCode== RESULT_OK){
            val uri: Uri? = resData?.data
            if (uri != null) {
                readExcelFile(uri)
            } else Toast.makeText(this, "The excel sheet is empty!", Toast.LENGTH_SHORT).show()
        }
    }
}