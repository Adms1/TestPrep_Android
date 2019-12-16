package com.testcraft.testcraft.models

class MyPackageModel {

    var Status = ""
    var data: ArrayList<MyPkgData> = ArrayList()

    class MyPkgData {

        var ID = 0
        var Name = ""
        var Icon = ""
        var StandardName = ""
        var StandardID = ""
        var BoardID = ""
        var isCompetitive = false
        var PackageList: ArrayList<PackageData.PackageDataList> = ArrayList()
        var TestSummary: ArrayList<testSummary> = ArrayList()

    }

    class testSummary {

        var count = 0
        var status = ""

    }

}