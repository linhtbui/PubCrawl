package us.grinnell.myweather

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import net.glxn.qrgen.android.QRCode

class QRCodeActivity : AppCompatActivity() {
    private var mEditCode: EditText? = null
    private var mButtonCreate: Button? = null
    private var mImagePreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_code)

        mButtonCreate = findViewById(R.id.buttonCreate) as Button
        mImagePreview = findViewById(R.id.imagePreview) as ImageView

        (mButtonCreate as Button).setOnClickListener {
            val num = Math.random()
            val text = num.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, "enter text to create barcode",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /*
            * Generate bitmap from the text provided,
            * The QR code can be saved using other methods such as stream(), file(), to() etc.
            * */
            val bitmap = QRCode.from(text).withSize(1000, 1000).bitmap()
            (mImagePreview as ImageView).setImageBitmap(bitmap)
            hideKeyboard()
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}