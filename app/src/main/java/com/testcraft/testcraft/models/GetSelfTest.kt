package com.testcraft.testcraft.models

class GetSelfTest {

    var Status = ""
    var Msg = ""
    var data: ArrayList<GetSelfData> = ArrayList()

    class GetSelfData{

        var StudentTestPackageID = 0
        var TestPackageName = ""
        var NumberOfTest = ""
        var NumberOfComletedTest = ""
        var TestPackageSalePrice = ""
        var PurchaseDate = ""
        var ExpirationDate = ""
        var Icon = ""
        var STPGuID = ""
        var TutorName = ""

    }

}