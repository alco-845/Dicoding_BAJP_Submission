package com.alcorp.moviecatalogue.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.ui.favorite.FavoriteActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_share).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_fav){
            customDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun customDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(true)

        val movie = dialog.findViewById(R.id.cMovie) as ConstraintLayout
        val tv = dialog.findViewById(R.id.cTvShow) as ConstraintLayout

        movie.isEnabled = true
        tv.isEnabled = true

        movie.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            intent.putExtra(FavoriteActivity.EXTRA_TYPE, resources.getString(R.string.movie))
            startActivity(intent)
        }

        tv.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            intent.putExtra(FavoriteActivity.EXTRA_TYPE, resources.getString(R.string.tvshow))
            startActivity(intent)
        }

        dialog.show()
    }
}