package com.pdinc.weather.utils

class convertKelvin {
    private fun convertdegree(temp:Float):Float{
        var k= 273.15F
        k=temp-k
        return k
    }

}