package net.squanchy.about.licenses

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_licenses.*
import net.squanchy.R
import net.squanchy.support.view.CardSpacingItemDecorator

class LicensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_licenses)
        setupToolbar()

        librariesList.adapter = LibrariesAdapter(this)
        librariesList.layoutManager = LinearLayoutManager(this)

        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.card_horizontal_margin)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.card_vertical_margin)
        librariesList.addItemDecoration(CardSpacingItemDecorator(horizontalSpacing, verticalSpacing))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
