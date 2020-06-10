package com.testcraft.testcraft.models

class GetMarketPlaceData {

    var Status = ""
    var Msg = ""
    var data: GetData = GetData()

    class GetData {

        var TestPackage: ArrayList<PackageData.PackageDataList> = ArrayList()
        var Tutors: ArrayList<PackageData.PackageDataList> = ArrayList()
        var FreeTestPackage: ArrayList<PackageData.PackageDataList> = ArrayList()
        var SingleTestPackage: ArrayList<PackageData.PackageDataList> = ArrayList()

        var BannerList: ArrayList<bannerurl> = ArrayList()
        var Subscription: ArrayList<bannerurl> = ArrayList()

        class bannerurl {
            var BannerURL = ""
        }
    }
}
