package com.example.models

class Company(
    var id: Int,
    var title: String,
    var upper_company_id: Int,
    var departments: ArrayList<Department>,
    var companies: ArrayList<Company>
)

data class Department(
    val id: Int,
    val title: String,
)

