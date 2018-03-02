package com.example.k_zaiem.myapplicationdb

/**
 * Created by K_Zaiem on 27/02/2018.
 */
class ProfileUploadInfo {


    lateinit   var profilName: String
    lateinit   var profilTel: String
    lateinit var Date: String

    lateinit var imageURL: String

    constructor() {

    }

    constructor(name: String,tel:String,date:String,url: String) {

        this.profilTel=tel
        this.Date=date
        this.profilName = name
        this.imageURL = url
    }
}