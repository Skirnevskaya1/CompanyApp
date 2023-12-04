package com.e.companyapp.view.menu

interface IAppMainMenu {
    fun openMainMenu(string: String)
    fun openFragment(nameFragment: String)
    fun openMain(nameFragment: String)
    fun backFragment(nameFragment: String)
}