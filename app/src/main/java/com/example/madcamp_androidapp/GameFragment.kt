package com.example.madcamp_androidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.util.Log
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.util.TypedValue
import java.lang.Math
import com.example.madcamp_androidapp.databinding.FragmentGameBinding
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout


class GameFragment : Fragment() {

    private lateinit var view: View // GameFragment의 View

    // 돼지 키우기 관련
    private lateinit var pigButton: ImageButton // 키우는 돼지
    private lateinit var feedBottle: ImageView // baby일 때 주는 젖병 (움직임O)
    private lateinit var feedBowl: ImageView // middle일 때 주는 사료통 (정지)
    private lateinit var feedSeed: ImageView // middle일 때 주는 씨앗 (움직임O)
    private lateinit var feedHayBowl: ImageView // fatty일 때 주는 건초통 (정지)
    private lateinit var feedHay: ImageView // fatty일 때 주는 건초 (움직임O)

    private lateinit var countText: TextView // 먹이 얼마나 줬는지 나타내는 text

    // level up 할 때 반짝 나타나는 text
    // 및 친밀도 증가할 때마다 샬롯이 윌버에게 주는 한글
    private lateinit var levelUpTextView: TextView


    //친밀도 관련
    private lateinit var webLine: ImageView // 길이 늘어나는 거미줄 line
    private lateinit var spider: ImageView // 거미
    private lateinit var webButton: ImageButton // 상단 거미줄 부분
    private lateinit var friendText: TextView // 친밀도 관련 설명 text
    // 친밀도 단계 나타내는 오른쪽 상단 하트
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView
    // 친밀도 만땅일 때 눈알 하트 뿅뿅
    private lateinit var friend1: TextView
    private lateinit var friend2: TextView

    
    //변수 및 핸들러
    private var feed: Int = 0 // 먹이 얼마나 줬는지 저장하는 변수
    private var friend: Int = 0 // 친밀도 얼마나 저장했는지
    private val handler = Handler() // Level Up! 표시하기 위해 필요한 핸들러


    //이미지 크기를 dp로 표현하고 싶어서 만든 변수 (애니메이션 때는 dp 사용 불가하기 때문)
    private val baby: Float = 200f
    private val middle: Float = 250f
    private val fatty: Float = 300f


    //마켓
    //private lateinit var binding: FragmentGameBinding
    private lateinit var marketButton: Button // 마켓으로 가는 버튼
    private lateinit var howMuchCoin: TextView // 코인이 얼마나 있는지 나타내는 텍스트뷰
    private var coin: Int = 0 // 마켓 이용 시 코인 수
    //모자를 샀는지 안 샀는지
    private var whetherHat1: Boolean = false
    private var whetherHat2: Boolean = false
    //실제 GameFragment에서 쓸 모자, 둘 중 하나만 true로 만들어서 true인 모자를 씀
    private var wearHat1: Boolean = false
    private var wearHat2: Boolean = false

    //모자
    private lateinit var hat: ImageView
    private lateinit var hatLayout: ConstraintLayout.LayoutParams


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //모든 변수들 초기화
        view = inflater.inflate(R.layout.fragment_game, container, false)
        pigButton = view.findViewById(R.id.btnLittlePig)
        countText = view.findViewById(R.id.tvCount)
        levelUpTextView = view.findViewById(R.id.tvLevelUp)
        feedBottle = view.findViewById(R.id.feedBottle)
        webLine = view.findViewById(R.id.line)
        spider = view.findViewById(R.id.spider)
        webButton = view.findViewById(R.id.btnWeb)
        friendText = view.findViewById(R.id.tvFriend)
        feedBowl = view.findViewById(R.id.feedBowl)
        feedSeed = view.findViewById(R.id.seed)
        feedHayBowl = view.findViewById(R.id.feedHay)
        feedHay = view.findViewById(R.id.hay)
        heart1 = view.findViewById(R.id.heart1)
        heart2 = view.findViewById(R.id.heart2)
        heart3 = view.findViewById(R.id.heart3)
        friend1 = view.findViewById(R.id.beFriends1)
        friend2 = view.findViewById(R.id.beFriends2)

        marketButton = view.findViewById(R.id.btnGomarket)
        howMuchCoin = view.findViewById(R.id.tvCoin)

        hat = view.findViewById(R.id.ivHat)
        hatLayout = hat.layoutParams as ConstraintLayout.LayoutParams


        //마켓 엑티비티로 넘어가는 버튼
        //marketButton = binding.btnGomarket
        marketButton.setOnClickListener {
            val intent = Intent(activity, GameMarketActivity::class.java)
            intent.putExtra("coin", coin)
            intent.putExtra("pigsize", feed)
            intent.putExtra("hat1", whetherHat1)
            intent.putExtra("hat2", whetherHat2)
            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }


        //친밀도 증가 부분 - 거미집 클릭
        webButton.setOnClickListener {

            shallot(view) // 샬롯이 거미줄을 늘려서 내려옴
            friend++ // 친밀도 1 증가

            if (friend == 1) {
                // 친밀도가 1일 때 -> "대단한 돼지" 한글 알려줌
                heart1.setImageResource(R.drawable.fullheart)
                levelUpTextView.text = "대단한 돼지"
                showLevelUpMessage(view)
            } else if (friend == 2) {
                // 친밀도가 2일 때 -> "멋진" 한글 알려줌
                heart2.setImageResource(R.drawable.fullheart)
                levelUpTextView.text = "멋진"
                showLevelUpMessage(view)
            } else if (friend == 3) {
                // 친밀도가 3일 때 -> 이제 절친!
                heart3.setImageResource(R.drawable.fullheart)
                levelUpTextView.text = "🕷️ 우리는 친구 🐷"
                showLevelUpMessage(view)
            } else if (friend >= 4) {
                // 친밀도는 3이 최대임
                levelUpTextView.text = "이미 절친이에요 🤪"
                showLevelUpMessage(view)
            }
        }
        
//        // 모자 set 하기
//        setHat(wearHat1, wearHat2, feed)

        //윌버 키우기 부분 - 윌버 클릭
        pigButton.setOnClickListener {
            if (countText.text == "한 번 더?\n 스테이크를 눌러주세요 🥩" || countText.text == "한 번 더?\n 윌버를 눌러주세요 🐽") {

                // 리셋할 때
                restartGame(view)

            } else {

                feed++ // 밥 준 횟수 1씩 증가
                coin++
                howMuchCoin.text = "$coin coins"
                //Log.w("feed: ", feed.toString())
                countText.text = "윌버가 $feed" +"번 밥을 먹었어요!"
                setHat(wearHat1, wearHat2, feed)

                if (feed < 3) {


                    // 아기 윌버 -> 밥 줄 때마다 젖병 움직임
                    bottle_front(view)

                } else if (feed == 3) {

                    // 아기 윌버에서 청소년 윌버로 성장

                    // level up 글씨 나타내기
                    levelUpTextView.text = "Level Up!"
                    showLevelUpMessage(view)

                    // 젖병 없애고 사료통 나타내기
                    feedBottle.visibility = View.GONE
                    feedSeed.visibility = View.VISIBLE
                    feedBowl.visibility = View.VISIBLE

                    // 아기 윌버를 청소년 윌버로 그림 변경하기
                    pigButton.setImageResource(R.drawable.middlepig) // 한 번 바뀌면 그 뒤는 안바뀜
                    pigButton.layoutParams.width = dp_to_px(middle) // 사이즈 키우기
                    pigButton.layoutParams.height = dp_to_px(middle) // 사이즈 키우기

                    // 청소년 윌버는 씨앗을 먹음
                    moving_seed(view)

                } else if (feed > 3 && feed < 6) {

                    // 씨앗 주는 함수
                    moving_seed(view)

                } else if (feed == 6) {

                    // 청소년 윌버에서 성인 윌버로 성장

                    // level up 글씨 나타내기
                    levelUpTextView.text = "Level Up!"
                    showLevelUpMessage(view)

                    // 사료통 없애고 건초 나타내기
                    feedSeed.visibility = View.GONE
                    feedBowl.visibility = View.GONE
                    feedHayBowl.visibility = View.VISIBLE
                    feedHay.visibility = View.VISIBLE

                    // 청소년 윌버를 성인 윌버로 그림 변경하기
                    pigButton.setImageResource(R.drawable.fattypig)
                    pigButton.layoutParams.width = dp_to_px(fatty) // 사이즈 조절
                    pigButton.layoutParams.height = dp_to_px(fatty) // 사이즈 조절

                    // 성인 윌버는 건초를 먹음
                    moving_hay(view)

                } else if (feed > 6 && feed < 10) {

                    // 건초 주는 함수
                    moving_hay(view)

                } else if (feed >= 10) {

                    // 다 자랐을 때

                    // 건초 더미들 지우기
                    feedHayBowl.visibility = View.GONE
                    feedHay.visibility = View.GONE

                    // 친밀도 증가시키는 거미줄 비활성화
                    webButton.isEnabled = false
                    // 윌버 버튼도 비활성화
                    pigButton.isEnabled = false
                    // 마켓 버튼도 비활성화
                    marketButton.isEnabled = false

                    if (friend < 3) {
                        hat.visibility = View.INVISIBLE
                        // 만약 친밀도가 부족하다면 스테이크로 변함
                        pigButton.setImageResource(R.drawable.steak)
                        pigButton.layoutParams.width = dp_to_px(baby) // 사이즈 조절
                        pigButton.layoutParams.height = dp_to_px(baby) // 사이즈 조절
                        countText.text = "샬롯과 윌버가 친해지지 못했네요 😭\n결국 윌버는 주인 입으로...😢"
                        handler.postDelayed( { countText.text = "한 번 더?\n 스테이크를 눌러주세요 🥩" }, 2000)
                    } else {
                        // 친밀도가 충분하다면 윌버는 살아남음
                        friend1.visibility = View.VISIBLE
                        friend2.visibility = View.VISIBLE
                        countText.text = "샬롯이 윌버를 구해줬어요 👏\n샬롯과 윌버는 평생 행복하게 살았답니다 🤗"
                        handler.postDelayed( { countText.text = "한 번 더?\n 윌버를 눌러주세요 🐽" }, 2000)
                    }
                    // 버튼을 눌러주세요 후에 다시 버튼 활성화
                    handler.postDelayed( { pigButton.isEnabled = true }, 2000)
                }
            }
        }
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val newcoin = it.getIntExtra("remain", coin)
                val newhat = it.getIntExtra("hat", 0)

                coin = newcoin
                howMuchCoin.text = "$coin coins"

                // 실제 hat 세팅은 onCreateView에서 하는게 맞는 듯. whetherHat의 Boolean 이용해서!
                if (newhat == 1) {
                    // hat1을 샀을 때
                    whetherHat1 = true
                    // 제일 마지막에 산 hat1을 씌움
                    wearHat1 = true
                    wearHat2 = false
                } else if (newhat == 2)  {
                    // hat2를 샀을 때
                    whetherHat2 = true
                    // 제일 마지막에 산 hat2를 씌움
                    wearHat1 = false
                    wearHat2 = true
                }

                // 모자 set 하기
                setHat(wearHat1, wearHat2, feed)

            }
        }
    }


    
    
    // 레벨업 했을 때 글씨 띄우기 및 친밀도 1씩 증가할 때 글씨 띄우기
    private fun showLevelUpMessage(view: View) {
        levelUpTextView.visibility = View.VISIBLE
        handler.postDelayed( { levelUpTextView.visibility = View.INVISIBLE }, 1500)
    }
    
    
    // 다시 시작할 때 초기 세팅으로 바꿔주기
    private fun restartGame(view: View) {
        
        // 아기 윌버로 버튼 이미지 다시 변경해주기
        pigButton.setImageResource(R.drawable.littlepig)
        pigButton.layoutParams.width = dp_to_px(baby)
        pigButton.layoutParams.height = dp_to_px(baby)
        
        // 친밀도 하트 다시 비우기
        heart1.setImageResource(R.drawable.emptyheart)
        heart2.setImageResource(R.drawable.emptyheart)
        heart3.setImageResource(R.drawable.emptyheart)
        
        // 윌버 눈 위 하트 뿅뿅 지우기
        friend1.visibility = View.INVISIBLE
        friend2.visibility = View.INVISIBLE
        
        // 친밀도 버튼 다시 활성화시키기
        webButton.isEnabled = true
        // 마켓 버튼도 다시 활성화시키기
        marketButton.isEnabled = true
        whetherHat1 = false
        whetherHat2 = false
        wearHat1 = false
        wearHat2 = false

        // 모자 지우기
        hat.visibility = View.INVISIBLE
        // 모자 사이즈 다시 초기화
        hat.layoutParams.width = dp_to_px(100f)
        hat.layoutParams.height = dp_to_px(100f)
        
        // 밥 준 횟수 및 친밀도 숫자 초기화
        feed = 0
        friend = 0
        coin = 0 // coin은 리셋하지 않고 계속 쓸 수 있도록할까?
        howMuchCoin.text = "$coin coins"
        countText.text = "윌버가 $feed" +"번 밥을 먹었어요!"
    }
    
    
    // 아기 윌버의 젖병 움직이기
    public fun bottle_front(view: View) {
        // 원래 위치에서 뒤쪽으로 움직임 (헷갈리게 만들어놨네...)
        val babybottleanim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_babybottle)
        feedBottle.startAnimation(babybottleanim)
    }


    // 청소년 윌버의 씨앗 움직이기
    public fun moving_seed(view: View) {
        val movingseedanim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_moveseed)
        feedSeed.startAnimation(movingseedanim)
    }


    // 성인 윌버의 건초 움직이기
    public fun moving_hay(view: View) {
        val movinghayanim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_movehay)
        feedHay.startAnimation(movinghayanim)
    }



    // 거미 위에서 아래로 움직이기
    public fun shallot(view: View) {
        // 아래 두 함수를 한 번에
        lineLonger(view)
        moveSpider(view)
    }
    public fun lineLonger(view: View) {
        // 거미줄 길게 만들기
        val linelongeranim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_webline)
        webLine.startAnimation(linelongeranim)
    }
    public fun moveSpider(view: View) {
        // 거미줄 내려가는 만큼 샬롯도 아래로 내려오게 만들기
        val moveSpider: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_movespider)
        spider.startAnimation(moveSpider)
    }

    
    
    // dp 단위를 px로 바꿔주기
    public fun dp_to_px(dpsize: Float): Int {
        val pxsize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, resources.displayMetrics)
        return Math.round(pxsize)
    }

    // 모자 씌우기
    public fun make_hat_fit(start: Float, top: Float) {
        hatLayout.marginStart = dp_to_px(start)
        hatLayout.topMargin = dp_to_px(top)
        hat.layoutParams = hatLayout
        hat.visibility = View.VISIBLE
    }
    public fun setHat(wearHat1: Boolean, wearHat2: Boolean, feed: Int) {
        if (wearHat1 && !wearHat2) {
            // hat1 쓸 때
            hat.setImageResource(R.drawable.hat1)
            if (feed < 3) {
                // 아기윌버, 200dp
                make_hat_fit(55f, -20f)
            } else if (feed < 6) {
                // 청소년 윌버, 250dp -> 1.25(131.25f, -22.5f) vs 1.125(118.125f, -20.25f)
                setMiddleHat()
                make_hat_fit(131.25f, -22.5f)
            } else if (feed <= 10) {
                // 성인 윌버, 300dp
                setFattyHat()
                make_hat_fit(75f, -67.5f)
            }
        } else if (!wearHat1 && wearHat2) {
            // hat2 쓸 때
            hat.setImageResource(R.drawable.hat2)
            if (feed < 3) {
                // 아기윌버, 200dp
                make_hat_fit(45f, -40f)
            } else if (feed < 6) {
                // 청소년 윌버, 250dp
                setMiddleHat()
                make_hat_fit(118.75f, -43.75f)
            } else if (feed <= 10) {
                // 성인 윌버, 300dp (67.5f, -105f) 인데 살짝만 내려도 ㄱㅊ을 듯
                setFattyHat()
                make_hat_fit(67.5f, -101f)
            }
        }
    }

    public fun setMiddleHat() {
        val middleHat: Float = 100f*(250f/200f)
        hat.layoutParams.width = dp_to_px(middleHat) // 사이즈 키우기
        hat.layoutParams.height = dp_to_px(middleHat) // 사이즈 키우기
    }

    public fun setFattyHat() {
        val FattyHat: Float = 100f*(300f/200f)
        hat.layoutParams.width = dp_to_px(FattyHat) // 사이즈 키우기
        hat.layoutParams.height = dp_to_px(FattyHat) // 사이즈 키우기
    }

    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 123
        private const val READ_CONTACTS_REQUEST_CODE = 1
    }
}