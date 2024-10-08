package com.example.epfcinit

import org.apache.poi.ss.usermodel.Cell
import java.util.Date

class EPFCData(
    custName:String,
    custNum:String,
    date: String,
    kwh: String,
    lag: String,
    lead: String,
) {
     val custName:String = custName
     val custNum: String = custNum
     val date: String = date
     val kwh:String = kwh
     val lag:String = lag
     val lead:String = lead
}
