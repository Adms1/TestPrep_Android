package com.testcraft.testcraft.models

class GetSubscriptionModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<GetSubscriptionData> = ArrayList()

    class GetSubscriptionData{

        var CourseTypeID = ""
        var CourseID = ""
        var BoardID = ""
        var StandardID = ""
        var BoardName = ""
        var StandardName = ""
        var CourseName = ""
        var CreateDate = ""
        var ExpiryDate = ""

    }

}