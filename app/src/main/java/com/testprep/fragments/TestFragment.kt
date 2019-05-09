package com.testprep.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import kotlinx.android.synthetic.main.fragment_test.*
import java.util.concurrent.TimeUnit

class TestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button2.setOnClickListener {
            setCountdown(editText2.text.toString())

        }
    }

    private fun setCountdown(minute: String) {

        val mili = TimeUnit.MINUTES.toMillis(minute.toLong())

        object : CountDownTimer(mili, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                textView.text = "" + String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisUntilFinished
                        )
                    )
                )
            }

            override fun onFinish() {
                textView.text = "done!"
            }
        }.start()
    }
}
