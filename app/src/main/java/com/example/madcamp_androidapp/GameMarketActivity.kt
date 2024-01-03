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

    lateinit var layoutHat: ConstraintLayout.LayoutParams // previewHat의 ConstraintLayout
    lateinit var previewHat: ImageView // 프리뷰 창의 모자
    lateinit var coinText: TextView // 잔여 coin의 수 나타내는 text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_market)

        // 모든 변수 세팅
        val view: View = findViewById(R.id.market_layout) // view
        coinText = findViewById(R.id.tv_coin) // 잔여 coin 수 나타내는 text
        val BackButton: ImageButton = findViewById(R.id.btn_return) // 뒤로 돌아가기 버튼
        val BuyHat1Button: Button = findViewById(R.id.btn_buy_hat1) // hat1 구매 버튼
        val BuyHat2Button: Button = findViewById(R.id.btn_buy_hat2) // hat2 구매 버튼
        val Hat1Button: ImageButton = findViewById(R.id.btn_hat1) // hat1 프리뷰 버튼
        val Hat2Button: ImageButton = findViewById(R.id.btn_hat2) // hat2 프리뷰 버튼
        previewHat = findViewById(R.id.iv_hat) // 프리뷰 창의 모자
        val pig: ImageView = findViewById(R.id.iv_pig) // 프리뷰 창의 윌버
        layoutHat = previewHat.layoutParams as ConstraintLayout.LayoutParams // previewHat의 ConstraintLayout

        var remainCoin: Int = 0 // 잔여 coin 수
        var howMuch: Int = 0 // 각 모자의 가격
        var whatHat: Int = 0 // 어떤 모자가 선택되었는지
        var whatPig = 0 // 현재 아기/청소년/성인 윌버 중에 어떤 건지

        val resultIntent = Intent() // 다시 fragment로 돌아갈 때 intent

        previewHat.visibility = View.INVISIBLE // 처음 세팅으로 모자는 invisible 해야함



        // GameFragment로부터 온 데이터들 처리

        // coin의 개수
        val coin = intent.getIntExtra("coin", 0)
        remainCoin += coin // remainCoin에 전달받은 coin의 수 추가
        coinText.text = "$remainCoin coins" // remainCoin 개수 나타내기

        // 모자를 이미 샀는지에 대한 여부
        val whetherHat1 = intent.getBooleanExtra("hat1", false) // hat1을 이미 샀는지
        val whetherHat2 = intent.getBooleanExtra("hat2", false) // hat2를 이미 샀는지
        if (whetherHat1) {
            // Hat1을 이미 샀으면
            BuyHat1Button.text = "구매 완료" // Buy라고 적힌 버튼을 구매 완료라고 바꿔주기
            BuyHat1Button.isEnabled = false // 버튼 비활성화
        }
        if (whetherHat2) {
            // Hat2를 이미 샀으면
            BuyHat2Button.text = "구매 완료" // Buy라고 적힌 버튼을 구매 완료라고 바꿔주기
            BuyHat2Button.isEnabled = false // 버튼 비활성화
        }

        // 마켓으로 넘어올 때 윌버가 얼마나 자랐는지 -> 프리뷰에 현재 상태 그대로 표시될 수 있도록
        val pigSize = intent.getIntExtra("pigsize", 0) // GameFragment의 feed와 같음
        if (pigSize < 3) {
            whatPig = 1 // 아기 윌버인 경우
            pig.setImageResource(R.drawable.littlepig) // 아기 윌버로 이미지 세팅
        } else if (pigSize < 6) {
            whatPig = 2 // 청소년 윌버인 경우
            pig.setImageResource(R.drawable.middlepig) // 청소년 윌버로 이미지 세팅
        } else {
            whatPig = 3 // 성인 윌버인 경우
            pig.setImageResource(R.drawable.fattypig) // 성인 윌버로 이미지 세팅
        }



        // 뒤로 가기 버튼
        BackButton.setOnClickListener {
            finish()
        }


        
        // Hat1 그림 선택했을 때 preview 나타내기 & 두 모자를 다 산 경우 GameFragment에 선택한 모자 반영하기
        Hat1Button.setOnClickListener {
            previewHat.setImageResource(R.drawable.hat1) // hat1으로 previewHat 세팅
            if (whatPig == 1) {
                preview_fit(55f, -20f) // 아기 윌버인 경우
            } else if (whatPig == 2) {
                preview_fit(105f, -18f) // 청소년 윌버인 경우
            } else if (whatPig == 3) {
                preview_fit(50f, -45f) // 성인 윌버인 경우
            }
            if (whetherHat1 && whetherHat2) {
                // 두 제품을 이미 다 산 경우에, 한 번 더 hat1 그림을 선택한다면
                whatHat = 1 // GameFragment의 실제 윌버가 hat1을 쓰도록 Intent로 전달
                resultIntent.putExtra("remain", remainCoin) // coin의 수 전달
                resultIntent.putExtra("hat", whatHat) // 어떤 hat 선택했는지 전달
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // 마켓 종료
            }
        }

        // Hat2 그림 선택했을 때 preview 나타내기 & 두 모자를 다 산 경우 GameFragment에 선택한 모자 반영하기
        Hat2Button.setOnClickListener {
            previewHat.setImageResource(R.drawable.hat2) // hat2으로 previewHat 세팅
            if (whatPig == 1) {
                preview_fit(45f, -40f) // 아기 윌버인 경우
            } else if (whatPig == 2) {
                preview_fit(95f, -35f) // 청소년 윌버인 경우
            } else if (whatPig == 3) {
                preview_fit(45f, -70f) // 성인 윌버인 경우
            }
            if (whetherHat1 && whetherHat2) {
                // 두 제품을 이미 다 산 경우에, 한 번 더 hat2 그림을 선택한다면
                whatHat = 2 // GameFragment의 실제 윌버가 hat2을 쓰도록 Intent로 전달
                resultIntent.putExtra("remain", remainCoin) // coin의 수 전달
                resultIntent.putExtra("hat", whatHat) // 어떤 hat 선택했는지 전달
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // 마켓 종료
            }
        }



        // Hat1 Buy 버튼 눌렀을 때
        BuyHat1Button.setOnClickListener {
            howMuch = 2 // hat1의 가격 설정
            val returnCoin1 = buy(remainCoin, howMuch) // hat1을 사고 나서 남은 coin 수
            if (remainCoin > returnCoin1) {
                // returnCoin이 더 적다는 건 구매했다는 의미
                whatHat = 1 // hat1을 샀음을 표시
                remainCoin = returnCoin1 // hat1을 사고 나서 남은 coin의 수를 remainCoin에 반영
                coinText.text = "$remainCoin coins" // remainCoin 수 나타내기
                BuyHat1Button.text = "구매 완료" // "Buy"라고 적힌 버튼 "구매 완료"로 바꾸기
                BuyHat1Button.isEnabled = false // "Buy" 버튼 비활성화

                resultIntent.putExtra("remain", remainCoin) // remainCoin의 수 전달
                resultIntent.putExtra("hat", whatHat) // 어떤 hat 선택했는지 전달
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // 마켓 종료
            }
            remainCoin = returnCoin1 // 구매하지 않은 경우는 returnCoin1은 입력된 그대로 반환됨
            coinText.text = "$remainCoin coins" // remainCoin 수 나타내기
        }

        // Hat2 Buy 버튼 눌렀을 때
        BuyHat2Button.setOnClickListener {
            howMuch = 6 // hat2의 가격 설정
            val returnCoin2 = buy(remainCoin, howMuch) // hat2를 사고 나서 남은 coin 수
            if (remainCoin > returnCoin2) {
                // returnCoin이 더 적다는 건 구매했다는 의미
                whatHat = 2 // hat2를 샀음을 표시
                remainCoin = returnCoin2 // hat2를 사고 나서 남은 coin의 수를 remainCoin에 반영
                coinText.text = "$remainCoin coins" // remainCoin 수 나타내기
                BuyHat2Button.text = "구매 완료" // "Buy"라고 적힌 버튼 "구매 완료"로 바꾸기
                BuyHat2Button.isEnabled = false // "Buy" 버튼 비활성화

                resultIntent.putExtra("remain", remainCoin) // remainCoin의 수 전달
                resultIntent.putExtra("hat", whatHat) // 어떤 hat 선택했는지 전달
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // 마켓 종료
            }
            remainCoin = returnCoin2 // 구매하지 않은 경우는 returnCoin2는 입력된 그대로 반환됨
            coinText.text = "$remainCoin coins" // remainCoin 수 나타내기
        }



    }



    // 모자의 위치 세팅을 위해 dp를 px로 변환해주는 함수 (GameFragment와 동일)
    fun dp_to_px(dpsize: Float): Int {
        val pxsize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, resources.displayMetrics)
        return Math.round(pxsize)
    }


    // 모자 위치 세팅해주는 함수 (GameFragment와 동일)
    fun preview_fit(start: Float, top: Float) {
        layoutHat.marginStart = dp_to_px(start)
        layoutHat.topMargin = dp_to_px(top)
        previewHat.layoutParams = layoutHat
        previewHat.visibility = View.VISIBLE
    }


    // 모자 구매
    fun buy(remainCoin: Int, howMuch: Int): Int {
        var returnCoin: Int = 0 // return 해 줄 coin의 수 변수 설정
        if (remainCoin < howMuch) {
            // 보유한 코인의 수보다 모자가 더 비쌀 때
            returnCoin = remainCoin // return 해 줄 coin의 수는 remainCoin 그대로
            Toast.makeText(this, "코인이 부족합니다", Toast.LENGTH_SHORT).show() // coin이 부족하다는 토스트 메세지
        } else {
            // 보유한 코인으로 모자를 살 수 있을 때
            returnCoin = remainCoin - howMuch // returnCoin은 원래 코인의 수에서 모자의 값을 뺀 것
        }
        return returnCoin
    }
}