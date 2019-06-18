package com.testprep.models

import java.io.Serializable

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
        var TestType: ArrayList<PackageTestType> = ArrayList()

        var PaymentTransactionID = ""
        var PaymentAmount = 0
        var PaymentDate = ""
        var ExternalTransactionID = ""
        var ExternalTransactionStatus = ""

        var StudentTestPackageID = 0
        var PurchaseDate = ""
        var ExpirationDate = ""
    }

    class PackageTestType(var name: String, var qty: String) : Serializable {

        var TestTypeName = ""
        var TestQuantity = ""

    }

}
