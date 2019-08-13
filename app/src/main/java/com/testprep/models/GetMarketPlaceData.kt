package com.testprep.models

class GetMarketPlaceData {

    var Status = ""
    var Msg = ""
    var data: GetData = GetData()

    class GetData {
        var TestPackage: ArrayList<PackageData.PackageDataList> = ArrayList()
        var Tutors: ArrayList<PackageData.PackageDataList> = ArrayList()

    }
}
