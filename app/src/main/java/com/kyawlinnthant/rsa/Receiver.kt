package com.kyawlinnthant.rsa

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_receiver.*


class Receiver : BaseFragment() {

    private var hasKey = false
    private var hasMsg = false

    override val layoutId: Int get() = R.layout.layout_receiver

    override fun setupView() {

        (activity as AppCompatActivity).app_toolbar.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.colorAccent
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val win = (activity as AppCompatActivity).window
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            win.statusBarColor = ContextCompat.getColor(context!!, R.color.colorAccentDark)

        }

        btnEnable(false)
        viewLogic()
        button.setOnClickListener { process() }
    }

    private fun process() {

        val receiverKey = Base64.encodeToString(etKey.text.toString().toByteArray(),Base64.DEFAULT)
        Log.e("receiverKey",receiverKey)
        val receiverSize = etMsg.text.toString().last()
        val sizeString = receiverSize.toString()
        val sizeInt = sizeString.toInt()
        val subSize = (sizeInt/3+1)*4
        val deMes = etMsg.text!!.substring(0, etMsg.length() - subSize-1)
        val msgKey = etMsg.text!!.removeRange(0, etMsg.length() - subSize-1).toString()
        val key = msgKey.substring(0,msgKey.length-1)

        Log.e("msgKey",key)

        if (receiverKey.trim() == key.trim()){
            decrypt(deMes)
        }else Snackbar.make(view!!,"Wrong Key",Snackbar.LENGTH_LONG).show()

    }

    private fun decrypt(msg: String) {

        result.visibility = View.VISIBLE

        val rsa = RSAPadding(context!!)
        result.text = rsa.decrypt(msg)
    }

    private fun viewLogic() {
        etKey.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasKey = s!!.trim().isNotEmpty()
                checkBtn()
            }
        })

        etMsg.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hasMsg = s!!.trim().isNotEmpty()
                checkBtn()
            }
        })
    }

    override fun listen() {

    }

    private fun checkBtn() = btnEnable(hasKey and hasMsg)

    private fun btnEnable(flag: Boolean) {
        if (flag) {
            button.apply {
                this.isEnabled = flag
                this.alpha = 1.0f
            }
        } else {
            button.apply {
                this.isEnabled = flag
                this.alpha = 0.7f
            }
        }
    }
}