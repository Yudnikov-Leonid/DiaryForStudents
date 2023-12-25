package com.maxim.diaryforstudents.news.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.maxim.diaryforstudents.news.presentation.Reload

interface NewsCloudDataSource {
    fun init(reload: Reload)
    fun data(): List<NewsData>

    class Base(
        private val dataBase: DatabaseReference
    ): NewsCloudDataSource {
        private val news = mutableListOf<NewsData>()
        private var cachedListener: Pair<Query, ValueEventListener>? = null
        override fun init(reload: Reload) {
            if (cachedListener != null) {
                cachedListener!!.first.removeEventListener(cachedListener!!.second)
                cachedListener = null
            }
            val query = dataBase.child("news")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        it.getValue(NewsData.Base::class.java)!!
                    }
                    news.clear()
                    news.addAll(data.sortedByDescending { it.date })
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            }
            query.addValueEventListener(listener)
            cachedListener = Pair(query, listener)
        }

        override fun data() = news
    }
}