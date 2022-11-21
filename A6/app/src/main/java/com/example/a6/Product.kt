package com.example.a6

class Product {
    var id: Int = 0
    var name: String? = null
    var quantity: Int = 0

    constructor(id: Int, name: String, quantity: Int) {
        this.id = id
        this.name = name
        this.quantity = quantity
    }

    constructor(name: String, quantity: Int) {
        this.name = name
        this.quantity = quantity
    }

    override fun toString(): String {
        return "Product(id=$id, name=$name, quantity=$quantity)"
    }
}