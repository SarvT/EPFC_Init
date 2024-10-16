package com.example.epfcinit
object DataList {
    val dataList: MutableList<EPFCData> = mutableListOf()
    private val listeners = mutableListOf<() -> Unit>()

    fun addData(task: EPFCData) {
        dataList.add(task)
        notifyListeners()
    }

    fun updateData(index: Int, task: EPFCData) {
        dataList[index] = task
        notifyListeners()
    }

    fun removeData(task: EPFCData) {
        dataList.remove(task)
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }
}
