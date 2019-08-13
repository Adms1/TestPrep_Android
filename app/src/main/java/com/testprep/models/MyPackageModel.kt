package com.testprep.models

class MyPackageModel {

    var Status = ""
    var data: ArrayList<MyPkgData> = ArrayList()

    class MyPkgData {

        var ID = ""
        var Name = ""
        var Icon = ""
        var isCompetitive = false
        var PackageList: ArrayList<PackageData.PackageDataList> = ArrayList()

    }

}
