package com.kyawlinnthant.rsa

import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_sender.*

class Sender : BaseFragment() {

    private var hasPhone: Boolean = false
    private var hasKey: Boolean = false
    private var hasMsg: Boolean = false

    override val layoutId: Int get() = R.layout.layout_sender

    override fun setupView() {
        (activity as AppCompatActivity).app_toolbar.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.colorPrimary
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val win = (activity as AppCompatActivity).window
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            win.statusBarColor = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)

        }

        btnEnable(false)
        button.setOnClickListener { sendMsg() }

        viewLogic()
    }

    override fun listen() {

    }

    private fun viewLogic() {

        etMsg.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasMsg = s!!.trim().isNotEmpty()
                checkBtn()
                showPhone(s.trim().isNotEmpty())
                show(s.toString())

            }
        })

        etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasPhone = s!!.trim().isNotEmpty()
                checkBtn()
                tiPhone.error = "Fill Valid Phone Number!"
                tiPhone.isErrorEnabled = !isPhoneNumberValid(s.trim().toString())

            }
        })

        etKey.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                hasKey = s!!.trim().isNotEmpty()
                checkBtn()

                etMsg.isEnabled = s.trim().isNotEmpty()
            }
        })

    }

    private fun showPhone(flag: Boolean) {
        if (flag) {
            tvHint.visibility = View.VISIBLE
            tvText.visibility = View.VISIBLE
        } else {
            tvHint.visibility = View.GONE
            tvText.visibility = View.GONE
        }
    }

    private fun checkBtn() = btnEnable(hasPhone and hasKey and hasMsg)
    private fun btnEnable(flag: Boolean) {
        button.isEnabled = flag
        if (flag) button.alpha = 1.0f else button.alpha = 0.7f
    }

    private fun sendMsg() {

        encrypt(etMsg.text!!.trim().toString())

        Intent(Intent.ACTION_VIEW).apply {
            this.type = "vnd.android-dir/mms-sms"
            this.putExtra("address", etPhone.text!!.trim().toString())
            this.putExtra(
                "sms_body",
                "key>>" + etKey.text.toString() +"\n" + "message>>"+encrypt(etMsg.text.toString())
            )
            startActivity(this)
        }



    }

    private fun getKey(input: String): String = Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)

    private fun encrypt(msg: String) : String {

        val rsa = RSAPadding(context!!)
        val ans = rsa.encrypt(msg)+getKey(etKey.text.toString())+etKey.text!!.length.toString()
        return ans
    }

    private fun show(msg: String){
        val rsa = RSAPadding(context!!)
        val ans = rsa.encrypt(msg)+getKey(etKey.text.toString())+etKey.text!!.length.toString()
        tvText.text = ans
        Log.e("rsa",rsa.encrypt(msg))
        Log.e("getkey",getKey(etKey.text.toString())+etKey.text!!.length.toString())
        Log.e("input","${ans.length}")
        Log.e("ans",ans)
    }


}