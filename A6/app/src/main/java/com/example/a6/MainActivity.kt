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
    private lateinit var nameET: EditText
    private lateinit var quantityET: EditText
    private lateinit var addBTN: Button
    private lateinit var findBTN: Button
    private lateinit var deleteBTN: Button
    private lateinit var outputTV: TextView
    private lateinit var productsRV: RecyclerView

    private lateinit var dbHandler : MyDBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameET = findViewById(R.id.name)
        quantityET = findViewById(R.id.quantity)
        addBTN = findViewById(R.id.add)
        findBTN = findViewById(R.id.find)
        deleteBTN = findViewById(R.id.delete)
        outputTV = findViewById(R.id.output)
        productsRV = findViewById(R.id.products)

        dbHandler = MyDBHandler(this, null, null, 1)
        val products = dbHandler.findAllProducts()
        productsRV.adapter = ItemAdapter(this, products)
        }

    fun newProduct(view: View) {
        val quantity = Integer.parseInt(quantityET.text.toString())
        val product = Product(nameET.text.toString(), quantity)
        dbHandler.addProduct(product)
        nameET.setText("")
        quantityET.setText("")
        productsRV.adapter?.notifyDataSetChanged()
    }

    fun lookupProduct(view: View) {
        val product = dbHandler.findProduct(nameET.text.toString())
        if (product != null) {
            outputTV.text = product.toString()
        } else {
            outputTV.text = "No Match Found"
        }
    }

    fun removeProduct(view: View) {
        dbHandler.deleteProduct(nameET.text.toString())
        productsRV.adapter?.notifyDataSetChanged()
        nameET.setText("")
        quantityET.setText("")
    }
}