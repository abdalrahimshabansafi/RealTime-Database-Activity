package com.example.realtimeexample

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.realtimeexample.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var count = 0
    private val database = Firebase.database
    var databaseReference: DatabaseReference? = null
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var adapter: RecAdapter
    var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        dataList = ArrayList()
        adapter = RecAdapter(this@MainActivity, dataList)
        binding!!.listData.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val gridLayoutManager = GridLayoutManager(this@MainActivity, 1)
        binding!!.listData.layoutManager = gridLayoutManager

        binding!!.buttonAdd.setOnClickListener {
            if (check()) {
                saveDataInRealTime()
            }
        }
        binding!!.buttonRead.setOnClickListener {
            // Read from the database
            databaseReference!!.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dataList.clear()
                    for (itemSnapshot in snapshot.children) {
                        val dataClass = itemSnapshot.getValue(DataClass::class.java)
                        if (dataClass != null) {
                            dataList.add(dataClass)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,"Read Data, Failed!",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
        private fun saveDataInRealTime() {
            val naem = binding!!.addName.text.toString()
            val number = binding!!.addNum.text.toString()

            val dataClass = DataClass(naem, number)
            val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

            FirebaseDatabase.getInstance().getReference("Users").child(currentDate)
                .setValue(dataClass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Add New User, Success!", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@MainActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    private fun check():Boolean{
        if (binding!!.addName.text.toString() == ""){
            binding!!.textInputLayout.error = "You cannot leave User Name Empty!"
            return false
        }
        if (binding!!.addNum.text.toString() == ""){
            binding!!.textInputLayout01.error = "You cannot leave User Number Empty!"
            return false
        }
        return true
    }
}
