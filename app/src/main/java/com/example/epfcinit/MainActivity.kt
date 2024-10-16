package com.example.epfcinit

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.BaseColumns
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
    val dbHelper = DBHelper(this)

    companion object {
        const val REQUEST_CODE = 1 // Any unique integer value
    }

    private lateinit var binding:ActivityMainBinding
    private var epfcDataset: List<EPFCData> = emptyList()
//    DataL = emptyList()



    private var epfcAdapter=EPFCListAdapter(DataList.dataList)

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

        checkForPermission()
        setRecyclerView()
        DataList.addListener { updateRecyclerView() }
//        DataList.dataList = emptyList()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }





        binding.excelUploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            }
            pickExcelFile.launch(intent)
            Handler().postDelayed({
                val epfcAdapter = EPFCListAdapter(DataList.dataList)
                binding.epfcRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.epfcRecyclerView.adapter = epfcAdapter
            }, 2000)


        }

        binding.btnEnterDetails.setOnClickListener {
            val intent = Intent(this, EnterDetailsActivity::class.java)
//            intent.putExtras(this)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

        fun setRecyclerView() {
            val epfcAdapter = EPFCListAdapter(DataList.dataList)
            binding.epfcRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.epfcRecyclerView.adapter = epfcAdapter
            updateRecyclerView()
        }



    private fun addDataToDB(){
        //    DATABASE
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(EPFCContract.EPFCEntry.COLUMN_NAME_TITLE, "title")
            put(EPFCContract.EPFCEntry.COLUMN_NAME_SUBTITLE, "subTitle")
        }
        val newRowId = db.insert(EPFCContract.EPFCEntry.TABLE_NAME, null, values)
    }

    private fun getDataFromDB(){
        //    DATABASE
        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, EPFCContract.EPFCEntry.COLUMN_NAME_TITLE, EPFCContract.EPFCEntry.COLUMN_NAME_SUBTITLE)
        val selection = "${EPFCContract.EPFCEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf("My Title")
        val sortOrder = "${EPFCContract.EPFCEntry.COLUMN_NAME_SUBTITLE} DESC"
        val cursor = db.query(
            EPFCContract.EPFCEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )





//        val itemIds = mutableListOf<Long>()
//        with(cursor) {
//            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                itemIds.add(itemId)
//            }
//        }
//        cursor.close()


    }


    private fun deleteDataFromDB(){
        //    DATABASE
        val db = dbHelper.writableDatabase
        // Define 'where' part of query.
        val selection = "${EPFCContract.EPFCEntry.COLUMN_NAME_TITLE} LIKE ?"
// Specify arguments in placeholder order.
        val selectionArgs = arrayOf("MyTitle")
// Issue SQL statement.
        val deletedRows = db.delete(EPFCContract.EPFCEntry.TABLE_NAME, selection, selectionArgs)
    }


    private fun updateDataInDB(){
        val db = dbHelper.writableDatabase

// New value for one column
        val title = "MyNewTitle"
        val values = ContentValues().apply {
            put(EPFCContract.EPFCEntry.COLUMN_NAME_TITLE, title)
        }

// Which row to update, based on the title
        val selection = "${EPFCContract.EPFCEntry.COLUMN_NAME_TITLE} LIKE ?"
        val selectionArgs = arrayOf("MyOldTitle")
        val count = db.update(
            EPFCContract.EPFCEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs)
    }

    private fun updateRecyclerView() {
        epfcAdapter.update(DataList.dataList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK){
//            binding.epfcRecyclerView.adapter = EPFC(DataList.dataList)
//            epfcAdapter.notifyDataSetChanged()
            updateRecyclerView()
        }
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

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
//        epfcAdapter.update(DataList.dataList)
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


    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

        private fun readColumnFromExcelSheet(context: Context, inputStream: InputStream, columnIndex: Int)  {
            try {
//                DataList.dataList= emptyList()
//                DataList.dataList.clear()
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

                    DataList.dataList.add(data)
                    println("$cell1 $cell2")

                }
                updateRecyclerView()
                Toast.makeText(context, "Done extracting values.", Toast.LENGTH_SHORT).show()
workbook.close()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error reading Excel sheet ${e.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

}
