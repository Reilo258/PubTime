package com.example.pubtime

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi

val start: Int = 0
val end: Int = 20

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var pilkaCheckBox: CheckBox
    private lateinit var tenisCheckBox: CheckBox
    private lateinit var zuzelCheckBox: CheckBox
    private lateinit var szukajBtn: Button
    private lateinit var pubsListView: ListView

    private val puby = listOf(
        "pub superBowl - mecze piłkarskie",
        "pub superRakieta - mecze tenisa",
        "pub JackRussel - mecze piłkarskie",
        "pub footballGame - mecze piłkarskie",
        "pub Racing - zawody żużlowe",
        "pub biforek - mecze tenisowe",
        "pub ekstraGame - mecze piłkarskie, mecze tenisa, zawody żużlowe",
        "pub Wilki - mecze piłkarskie",
        "pub dzikie Koty - mecze piłkarskie",
        "pub czarno na białym - mecze tenisa"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        pilkaCheckBox = findViewById(R.id.pilkaCheckBox)
        zuzelCheckBox = findViewById(R.id.zuzelCheckBox)
        tenisCheckBox = findViewById(R.id.tenisCheckBox)
        szukajBtn = findViewById(R.id.button)
        pubsListView = findViewById(R.id.listView)

        seekBar.max = end/5
        seekBar.min = start

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Do nothing
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }
        })

        szukajBtn.setOnClickListener {
            refreshListy()
        }

        refreshListy()
    }

    private fun refreshListy() {
        val dystans = seekBar.progress * 5
        val pubyWzasiegu = pubyWzasiegu(dystans)
        val pofiltrowanePuby = filtruj(pubyWzasiegu)

        if (pofiltrowanePuby.isEmpty()) {
            pubsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOf("Brak barów w zadanej odległości :("))
        }
        else
            pubsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pofiltrowanePuby)
    }

    private fun pubyWzasiegu(dystans: Int): List<String> {
        return when (dystans) {
            5 -> puby.subList(0, 3)
            10 -> puby.subList(0, 6)
            15 -> puby.subList(0, 8)
            20 -> puby
            else -> emptyList()
        }
    }

    private fun filtruj(pubs: List<String>): List<String> {
        val pokazPilka = pilkaCheckBox.isChecked
        val pokazZuzel = zuzelCheckBox.isChecked
        val pokazTenis = tenisCheckBox.isChecked
        val dystans = seekBar.progress * 5

        return pubs.filter {
            val isEkstraGame = it.contains("mecze piłkarskie, mecze tenisa, zawody żużlowe")
            val matchesFilters = (pokazPilka && it.contains("mecze piłkarskie")) ||
                    (pokazZuzel && it.contains("zawody żużlowe")) ||
                    (pokazTenis && it.contains("mecze tenisa"))

            // Sprawdzamy, czy pub to ekstraGame i czy wszystkie checkboxy są zaznaczone oraz dystans wynosi 20 km
            if (isEkstraGame) {
                pokazPilka && pokazZuzel && pokazTenis && dystans == 20
            } else {
                matchesFilters
            }
        }
    }
}