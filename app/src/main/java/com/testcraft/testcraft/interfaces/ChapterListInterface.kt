package com.testcraft.testcraft.interfaces

import com.testcraft.testcraft.models.GetChapterList

interface ChapterListInterface {

    fun getSelectedChapter(list: ArrayList<GetChapterList.GetChapterData>)

}