package com.testprep.models

import java.io.Serializable

class PackageData {

    var Status = ""
    var Msg = ""
    var data: ArrayList<PackageDataList> = ArrayList()

    class PackageDataList(imageResId: Int, TestPackageName: String) : Serializable {

        var imageResId = 0
        var TestPackageName = ""

        init {
            this.imageResId = imageResId
            this.TestPackageName = TestPackageName

        }

        var Name = ""
        var PackageType = 0
        var TestPackage: ArrayList<PackageDataList> = ArrayList()

        var TestPackageID = ""
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
        var InvoiceID = ""

        var SubjectName = ""
        var InstituteName = ""
        var TutorName = ""
        var TutorID = ""
        var StudentTestPackageID = 0
        var PurchaseDate = ""
        var ExpirationDate = ""

        var CourseTypeID = 0
        var CourseTypeName = ""
        var Icon = ""

        var TutorEmail = ""
        var TutorPhoneNumber = ""

        var isSelected = false

        var SubjectID = ""
        var StandardID = ""
        var StandardName = ""

        var CourseID = ""
        var CourseName = ""
    }

    class PackageTestType(var name: String, var qty: String) : Serializable {

        var TestTypeName = ""
        var TestQuantity = ""

    }

}
