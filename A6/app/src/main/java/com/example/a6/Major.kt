package com.example.a6

class Major {
    var id: Int = 0
    var name: String? = null
    var specialty: String? = null

    constructor(id: Int, name: String, specialty: String) {
        this.id = id
        this.name = name
        this.specialty = specialty
    }

    constructor(name: String, specialty: String) {
        this.name = name
        this.specialty = specialty
    }

    override fun toString(): String {
        return "Major(id=$id, name=$name, specialty=$specialty)"
    }
}