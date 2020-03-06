package com.testcraft.testcraft.models

class DeepLinkModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<DeepLinkData> = ArrayList()

    class DeepLinkData {

        var DeepLinkID = ""
        var TestPackageID = ""
        var language = ""
        var couponcode = ""
        var purchased = ""

    }

}
