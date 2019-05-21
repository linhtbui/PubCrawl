package us.grinnell.myweather

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)

        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                // start ShoppingListActivity
                var intentStart = Intent()
                intentStart.setClass(this@SplashScreenActivity, MapsActivity::class.java)
                startActivity(intentStart)
                //prevent user from clicking the back button into the splash screen
                finish()
            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })

        iconSplash.startAnimation(anim)

    }
}