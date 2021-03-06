package com.testcraft.testcraft.models

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
        var IsFree = ""

        var TestPackageID = ""
        var TestPackageSalePrice = ""
        var TestPackageListPrice = ""
        var TestPackageDescription = ""
        var NumberOfTest = ""
        var NumberOfComletedTest = ""
        var TestType: ArrayList<PackageTestType> = ArrayList()
        var TestList: ArrayList<PackageTestType> = ArrayList()

        var PaymentTransactionID = ""
        var PaymentAmount = 0
        var PaymentDate = ""
        var ExternalTransactionID = ""
        var ExternalTransactionStatus = ""
        var InvoiceID = ""
        var OrderID = ""
        var InvoiceGUID = ""
        var PackageName = ""

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

    class PackageTestType {

        var TestTypeName = ""
        var Name = ""
        var TestQuantity = ""

    }

}
