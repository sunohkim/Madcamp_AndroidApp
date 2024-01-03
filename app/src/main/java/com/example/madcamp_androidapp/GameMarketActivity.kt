package com.example.madcamp_androidapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.core.view.marginStart
import android.widget.Toast

class GameMarketActivity : AppCompatActivity() {

    lateinit var layoutHat: ConstraintLayout.LayoutParams
    lateinit var previewHat: ImageView
    lateinit var coinText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_market)

        val view: View = findViewById(R.id.market_layout)

        // 모든 변수 초기화
        coinText = findViewById(R.id.tv_coin)
        val BackButton: ImageButton = findViewById(R.id.btn_return)
        val BuyHat1Button: Button = findViewById(R.id.btn_buy_hat1)
        val BuyHat2Button: Button = findViewById(R.id.btn_buy_hat2)
        val Hat1Button: ImageButton = findViewById(R.id.btn_hat1)
        val Hat2Button: ImageButton = findViewById(R.id.btn_hat2)
        previewHat = findViewById(R.id.iv_hat)
        val pig: ImageView = findViewById(R.id.iv_pig)
        val mContext: Context = applicationContext
        layoutHat = previewHat.layoutParams as ConstraintLayout.LayoutParams

        var remainCoin: Int = 0
        var howMuch: Int = 0
        var whatHat: Int = 0

        // 다시 fragment로 돌아갈 때 intent
        val resultIntent = Intent()

        // 우선 모자 invisible
        previewHat.visibility = View.INVISIBLE


        // 게임 중 현재 코인 반영
        val coin = intent.getIntExtra("coin", 0) // GameFragment에서 전달받은 coin 수
        remainCoin = remainCoin + coin
        coinText.text = "$remainCoin coins"


        // 이미 모자를 샀다면
        val whetherHat1 = intent.getBooleanExtra("hat1", false)
        val whetherHat2 = intent.getBooleanExtra("hat2", false)
        if (whetherHat1) {
            // Hat1을 이미 샀으면
            BuyHat1Button.text = "구매 완료"
            BuyHat1Button.isEnabled = false
        }
        if (whetherHat2) {
            // Hat2를 이미 샀으면
            BuyHat2Button.text = "구매 완료"
            BuyHat2Button.isEnabled = false
        }


        // 초기 코인의 수에 따른 preview의 돼지 변화
        var whatPig = 0
        val pigSize = intent.getIntExtra("pigsize", 0)

        if (pigSize < 3) {
            // 아기 윌버인 경우
            whatPig = 1
            pig.setImageResource(R.drawable.littlepig)
        } else if (pigSize < 6) {
            // 청소년 윌버인 경우
            whatPig = 2
            pig.setImageResource(R.drawable.middlepig)
        } else {
            // 성인 윌버인 경우
            whatPig = 3
            pig.setImageResource(R.drawable.fattypig)
        }


        // 뒤로 가기 버튼
        BackButton.setOnClickListener {
            //previewHat.visibility = View.INVISIBLE
            finish()
        }


        // Hat1 이미지를 선택했을 때 preview
        Hat1Button.setOnClickListener {
            previewHat.setImageResource(R.drawable.hat1)
            if (whatPig == 1) {
                // 아기 윌버인 경우
                preview_fit(55f, -20f)
            } else if (whatPig == 2) {
                // 청소년 윌버인 경우
                preview_fit(105f, -18f)
            } else if (whatPig == 3) {
                // 성인 윌버인 경우
                preview_fit(50f, -45f)
            }
            
            // 만약 두 제품을 다 산 경우에는, preview 모자 그림을 누르면 모자가 바뀔 수 있도록
            if (whetherHat1 && whetherHat2) {
                whatHat = 1
                resultIntent.putExtra("remain", remainCoin)
                resultIntent.putExtra("hat", whatHat)

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // Hat2 이미지를 선택했을 때 preview
        Hat2Button.setOnClickListener {
            previewHat.setImageResource(R.drawable.hat2)
            if (whatPig == 1) {
                // 아기 윌버인 경우
                preview_fit(45f, -40f)
            } else if (whatPig == 2) {
                // 청소년 윌버인 경우
                preview_fit(95f, -35f)
            } else if (whatPig == 3) {
                // 성인 윌버인 경우
                preview_fit(45f, -70f)
            }

            // 만약 두 제품을 다 산 경우에는, preview 모자 그림을 누르면 모자가 바뀔 수 있도록
            if (whetherHat1 && whetherHat2) {
                whatHat = 2
                resultIntent.putExtra("remain", remainCoin)
                resultIntent.putExtra("hat", whatHat)

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        // Hat1 Buy 버튼 눌렀을 때
        BuyHat1Button.setOnClickListener {
            howMuch = 2
            val returnCoin1 = buy(remainCoin, howMuch)
            if (remainCoin > returnCoin1) {
                // returnCoin이 더 적다는 건 구매했다는 의미
                whatHat = 1
                remainCoin = returnCoin1
                coinText.text = "$remainCoin coins"
                
                // 나갔다가 다시 마켓으로 돌아왔을 때 구매 버튼이 비활성화 되어있어야 함
                // 이걸로는 안 되네...
                BuyHat1Button.text = "구매 완료"
                BuyHat1Button.isEnabled = false

                // fragment로 다시 돌려줘야 하는 값들은 remainCoin, whatHat
                resultIntent.putExtra("remain", remainCoin)
                resultIntent.putExtra("hat", whatHat)

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            remainCoin = returnCoin1
            coinText.text = "$remainCoin coins"
        }

        // Hat2 Buy 버튼 눌렀을 때
        BuyHat2Button.setOnClickListener {
            howMuch = 6
            val returnCoin2 = buy(remainCoin, howMuch)
            if (remainCoin > returnCoin2) {
                // returnCoin이 더 적다는 건 구매했다는 의미
                whatHat = 2
                remainCoin = returnCoin2
                coinText.text = "$remainCoin coins"

                // 나갔다가 다시 마켓으로 돌아왔을 때 구매 버튼이 비활성화 되어있어야 함
                // 이걸로는 안 되네...
                BuyHat2Button.text = "구매 완료"
                BuyHat2Button.isEnabled = false

                // fragment로 다시 돌려줘야 하는 값들은 remainCoin, whatHat
                resultIntent.putExtra("remain", remainCoin)
                resultIntent.putExtra("hat", whatHat)

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            remainCoin = returnCoin2
            coinText.text = "$remainCoin coins"
        }




    }

    fun dp_to_px(dpsize: Float): Int {
        val pxsize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, resources.displayMetrics)
        return Math.round(pxsize)
    }

    fun preview_fit(start: Float, top: Float) {
        layoutHat.marginStart = dp_to_px(start)
        layoutHat.topMargin = dp_to_px(top)
        previewHat.layoutParams = layoutHat
        previewHat.visibility = View.VISIBLE
    }

    fun buy(remainCoin: Int, howMuch: Int): Int {
        var returnCoin: Int = 0
        if (remainCoin < howMuch) {
            returnCoin = remainCoin
            Toast.makeText(this, "코인이 부족합니다", Toast.LENGTH_SHORT).show()
        } else {
            returnCoin = remainCoin - howMuch
        }
        //coinText.text = "$returnCoin coins"
        return returnCoin
    }
}