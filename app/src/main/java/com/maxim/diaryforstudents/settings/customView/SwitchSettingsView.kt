package com.maxim.diaryforstudents.settings.customView

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.maxim.diaryforstudents.core.App
import com.maxim.diaryforstudents.settings.data.SettingsStorage

class SwitchSettingsView: SwitchCompat {
    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    private lateinit var settings: SettingsStorage.Mutable

    fun init() {
        settings = (context.applicationContext as App).getCore().settingsStorage()
        isChecked = settings.read(id)
        setOnCheckedChangeListener { _, isChecked ->
            settings.set(id, isChecked)
        }
    }
}