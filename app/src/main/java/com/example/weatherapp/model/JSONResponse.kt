package com.example.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JSONResponse {

    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null

    @SerializedName("coord")
    @Expose
    var Coord: Coord? = null

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null

    @SerializedName("base")
    @Expose
    var base: String? = null

    @SerializedName("visibility")
    @Expose
    var visibility: String? = null

    @SerializedName("dt")
    @Expose
    var dt: String? = null

    @SerializedName("timezone")
    @Expose
    var timezone: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("cod")
    @Expose
    var cod: String? = null
}