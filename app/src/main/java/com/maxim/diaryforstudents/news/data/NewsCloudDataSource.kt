package com.maxim.diaryforstudents.news.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.news.presentation.Reload

interface NewsCloudDataSource {
    fun init(reload: Reload)
    fun data(): List<NewsData>

    class Base(
        private val dataBase: DatabaseReference
    ): NewsCloudDataSource {
        private val news = mutableListOf<NewsData>()
        override fun init(reload: Reload) {
            val query = dataBase.child("news")
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        it.getValue(NewsData.Base::class.java)!!
                    }
                    news.clear()
                    news.addAll(data.sortedByDescending { it.date })
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override fun data() = news
    }
}