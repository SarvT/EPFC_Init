package com.example.epfcinit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epfcinit.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var epfcDataset: List<EPFCData> = emptyList()
//    DataL = emptyList()

    private var epfcAdapter=EPFCListAdapter(epfcDataset)

    private val pickExcelFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedFileUri = data?.data
            selectedFileUri?.let { uri ->
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                inputStream?.let { stream ->
                    val columnIndex = 0
                readColumnFromExcelSheet(this, stream, columnIndex)!!
                } ?: run {
                    Toast.makeText(this, "Failed to open selected file", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

//        DataList.dataList = emptyList()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkForPermission()
        setRecyclerView()




        binding.excelUploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            }
            pickExcelFile.launch(intent)
            Handler().postDelayed({
                val epfcAdapter = EPFCListAdapter(epfcDataset)
                binding.epfcRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.epfcRecyclerView.adapter = epfcAdapter
            }, 2000)


        }

        binding.btnEnterDetails.setOnClickListener {
            val intent = Intent(this, EnterDetailsActivity::class.java)

            this.startActivity(intent)
        }
    }

        fun setRecyclerView() {
            val epfcAdapter = EPFCListAdapter(DataList.dataList)
            binding.epfcRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.epfcRecyclerView.adapter = epfcAdapter
        }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 1307) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permitted!!", Toast.LENGTH_SHORT).show()
                    Log.d("Permission", "Granted")

                } else {
                    Log.d("Permission", "Not Granted")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1307
                    )
                }
            }
        }

    private fun checkForPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1307
            )
        } else {

        }
    }

        private fun readColumnFromExcelSheet(context: Context, inputStream: InputStream, columnIndex: Int)  {
            try {
                DataList.dataList= emptyList()
                val workbook = WorkbookFactory.create(inputStream)

                val sheet = workbook.getSheetAt(0)

                var it =0
                for (row in sheet) {
                    if (it==0){
                        it++
                        continue
                    }
                    val cell1 = row.getCell(columnIndex)
                    val cell2 = row.getCell(columnIndex+1)
                    val cell3 = row.getCell(columnIndex+2)
                    val cell4 = row.getCell(columnIndex+3)
                    val cell5 = row.getCell(columnIndex+4)
                    val cell6 = row.getCell(columnIndex+5)
                    val data = EPFCData(
                        cell1.toString(), cell2.toString(), cell3.toString(),
                        cell4.toString(), cell5.toString(), cell6.toString())
                    DataList.dataList+=(data)
                    println("$cell1 $cell2")

                }
epfcAdapter.update(DataList.dataList)
                Toast.makeText(context, "Done extracting values.", Toast.LENGTH_SHORT).show()
workbook.close()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error reading Excel sheet ${e.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

}
