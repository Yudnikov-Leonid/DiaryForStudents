package com.maxim.diaryforstudents.news

interface NewsState {
    fun showList(adapter: NewsAdapter)
    data class Base(private val list: List<NewsUi>): NewsState {
        override fun showList(adapter: NewsAdapter) {
            adapter.update(list)
        }
    }
}