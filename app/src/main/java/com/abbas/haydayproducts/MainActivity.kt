package com.abbas.haydayproducts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val selected = linkedSetOf<String>()
    private val products = listOf(
        "🍞  خبز" to "Bread", "🥧  فطيرة" to "Pie",
        "🧀  جبن" to "Cheese", "🧈  زبدة" to "Butter",
        "🍬  سكر" to "Sugar", "🍿  فشار" to "Popcorn",
        "🍪  بسكويت" to "Cookie", "🍰  كعكة" to "Cake"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(0xFFF3F6F0.toInt())
        }
        root.addView(TextView(this).apply {
            text = "🌾 HAY DAY PRODUCTS BOT"
            textSize = 22f
            setTextColor(0xFF234B2B.toInt())
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "اختر المنتجات التي تريد تصنيعها"
            textSize = 17f
            setPadding(0, 24, 0, 12)
            setTextColor(0xFF263326.toInt())
        })
        products.forEach { (label, key) ->
            root.addView(Button(this).apply {
                text = if (selected.contains(key)) "🟢  $label" else label
                isAllCaps = false
                setOnClickListener {
                    if (!selected.add(key)) selected.remove(key)
                    render()
                }
            }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 6 })
        }
        root.addView(TextView(this).apply {
            text = "الحالة: متوقف"
            textSize = 16f
            setPadding(0, 14, 0, 8)
            setTextColor(0xFF263326.toInt())
        })
        root.addView(Button(this).apply {
            text = "إظهار أزرار التحكم فوق اللعبة"
            isAllCaps = false
            setOnClickListener { enableOverlay() }
        })
        root.addView(Button(this).apply {
            text = "▶ تشغيل البوت"
            isAllCaps = false
            setOnClickListener {
                if (selected.isEmpty()) {
                    Toast.makeText(this@MainActivity, "اختر منتجاً واحداً على الأقل", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity,
                        "تم اختيار ${selected.size} منتج؛ محرك التصنيع قيد الربط",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
        root.addView(Button(this).apply {
            text = "■ إيقاف"
            isAllCaps = false
            setOnClickListener { Toast.makeText(this@MainActivity, "متوقف", Toast.LENGTH_SHORT).show() }
        })
        setContentView(root)
    }

    private fun enableOverlay() {
        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this, FloatingControlService::class.java))
        } else {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        }
    }
}
