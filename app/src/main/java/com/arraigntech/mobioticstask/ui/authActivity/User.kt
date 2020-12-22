package com.arraigntech.mobioticstask.ui.authActivity

data class User(
    val userId: String,
    val name: String,
    val email: String
) {
    val isAuthenticated: Boolean = false
    var isNew: Boolean = false
    var isCreated: Boolean = false


}