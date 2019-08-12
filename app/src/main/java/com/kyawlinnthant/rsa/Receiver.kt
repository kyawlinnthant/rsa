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


        val inputKey = etKey.text.toString()
        val input64 = Base64.encodeToString(inputKey.toByteArray(), Base64.DEFAULT)
        val inputMsg = etMsg.text.toString()

        val msgLastIndex : Int = inputMsg.length-1
        val keyCountChar : Char = inputMsg[inputMsg.lastIndex]
        val keyCountString : String = keyCountChar.toString()
        val keyCountInt : Int = keyCountString.toInt()
        val a = keyCountInt-1
        val b : Int = a/3
        val c = b+1
        val d = c * 4
        val cutSize : Int = d
        val subSize = cutSize + 1
        val totalSubSize = msgLastIndex-subSize
        val keyStart = totalSubSize+1
        val keyEncrypt = inputMsg.substring(keyStart,msgLastIndex)
        val msgEncrypt = inputMsg.substring(0,keyStart)

        if (input64.trim()==keyEncrypt){
            decrypt(msgEncrypt)
        }else Snackbar.make(view!!,"Wrong Key",Snackbar.LENGTH_LONG).show()

        Log.e("deb","inputMsg${inputMsg.length}:$inputMsg:" +
                " keyCountChar$keyCountChar  : keyCountString$keyCountString  : keyCountInt$keyCountInt  " +
                "cutSize$cutSize   : a$a b$b c$c d$d subSize$subSize + keyEncrypt$keyEncrypt" +
                "msgEncrypt$msgEncrypt")

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