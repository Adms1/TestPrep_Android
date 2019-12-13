package com.testcraft.testcraft.models

class CouponModel {

    var Status = ""
    var Msg = ""
    var data: ArrayList<dataa> = ArrayList()

    class dataa {
        var CouponID = ""
        var CouponCode = ""
        var CouponTitle = ""
        var CouponDiscription = ""
        var DiscountPer = ""
        var ExpireDate = ""
    }

}
