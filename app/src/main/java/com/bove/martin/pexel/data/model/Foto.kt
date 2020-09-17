package com.bove.martin.pexel.data.model

/**
 * Created by Martín Bove on 28/05/2018.
 * E-mail: mbove77@gmail.com
 */
data class Foto(var id: Int, var width: Int, var height: Int, var url: String, var photographer: String, var original: String, var large: String, var large2x: String, var medium: String, var small: String, var portrait: String, var landscape: String, var tiny: String) {
    fun setName(id: Int) {
        this.id = id
    }
}