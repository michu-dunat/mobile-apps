package com.example.a6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a6.adapter.ItemAdapter

class MainActivity : AppCompatActivity() {
    lateinit var nameET : EditText
    lateinit var quantityET : EditText
    lateinit var addBTN : Button
    lateinit var findBTN : Button
    lateinit var deleteBTN : Button
    lateinit var outputTV : TextView
    lateinit var productsRV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameET = findViewById(R.id.name)
        quantityET = findViewById(R.id.quantity)
        addBTN = findViewById(R.id.add)
        findBTN = findViewById(R.id.find)
        deleteBTN = findViewById(R.id.delete)
        outputTV = findViewById(R.id.output)

        val dbHandler = MyDBHandler(this, null, null, 1)
        var myDataset = dbHandler.getAllProducts()

        val recyclerView = findViewById<RecyclerView>(R.id.products)
        recyclerView.adapter = myDataset?.let { ItemAdapter(this, it) }

    }

    fun newProduct(view: View) {
        val dbHandler = MyDBHandler(this, null, null, 1)

        val quantity = Integer.parseInt(quantityET.text.toString())

        val product = Product(nameET.text.toString(), quantity)

        dbHandler.addProduct(product)
        nameET.setText("")
        quantityET.setText("")

        println(dbHandler.getProductCount())

    }

    fun lookupProduct(view: View) {
        val dbHandler = MyDBHandler(this, null, null, 1)

        val product = dbHandler.findProduct(
            nameET.text.toString())

        if (product != null) {
            outputTV.text = product.toString()
        } else {
            outputTV.text = "No Match Found"
        }
    }

    fun removeProduct(view: View) {
        val dbHandler = MyDBHandler(this, null, null, 1)

        val result = dbHandler.deleteProduct(nameET.text.toString())

        if (result) {
            outputTV.text = "Record Deleted"
            nameET.setText("")
            quantityET.setText("")
        } else
            outputTV.text = "No Match Found"
    }
}