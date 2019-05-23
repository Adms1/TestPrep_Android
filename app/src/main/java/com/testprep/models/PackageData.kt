package com.testprep.models

class PackageData {

    var Status = ""
    var Msg = ""
    var data: ArrayList<PackageDataList> = ArrayList()

    class PackageDataList {

        var TestPackageID = ""
        var TestPackageName = ""
        var TestPackageSalePrice = ""
        var TestPackageListPrice = ""
        var TestPackageDescription = ""
        var NumberOfTest = ""

    }

    class PackageTestType {

        var TestTypeName = ""
        var TestQuantity = ""

    }

}
