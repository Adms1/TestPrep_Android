package com.testcraft.testcraft.models

class GetChapterList {

    var Status = ""
    var Msg = ""
    var data: ArrayList<GetChapterData> = ArrayList()

    class GetChapterData{

        var ID = ""
        var Name = ""
        var isSelected = false

    }

}