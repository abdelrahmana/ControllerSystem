package com.smartangle.controllersystemapp.admin.settings.editpassword

data class EditPasswordRequest(
    var old_password: String?="",
    var password: String?="",
    var password_confirmation: String?=""
)