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
    private lateinit var pigButton: ImageButton // 키우는 돼지 (윌버)
    private lateinit var feedBottle: ImageView // baby일 때 주는 젖병 (움직임O)
    private lateinit var feedBowl: ImageView // middle일 때 주는 사료통 (정지)
    private lateinit var feedSeed: ImageView // middle일 때 주는 씨앗 (움직임O)
    private lateinit var feedHayBowl: ImageView // fatty일 때 주는 건초통 (정지)
    private lateinit var feedHay: ImageView // fatty일 때 주는 건초 (움직임O)
    private lateinit var countText: TextView // 먹이 얼마나 줬는지 나타내는 text

    // level up 할 때 반짝 나타나는 text
    // 및 친밀도 증가할 때마다 샬롯이 윌버에게 주는 한글
    private lateinit var levelUpTextView: TextView


    // 친밀도 관련
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

    
    // 변수 및 핸들러
    private var feed: Int = 0 // 먹이 얼마나 줬는지 저장하는 변수
    private var friend: Int = 0 // 친밀도 얼마나 저장했는지
    private val handler = Handler() // Level Up! 표시하기 위해 필요한 핸들러


    // 이미지 크기를 dp로 표현하고 싶어서 만든 변수 (애니메이션 때는 dp 사용 불가하기 때문)
    // 사실 안 만들어도 되기는 해...
    private val baby: Float = 200f
    private val middle: Float = 250f
    private val fatty: Float = 300f


    // 마켓
    private lateinit var marketButton: Button // 마켓으로 가는 버튼
    private lateinit var howMuchCoin: TextView // 코인이 얼마나 있는지 나타내는 텍스트뷰
    private var coin: Int = 0 // 마켓 이용 시 코인 수
    // 모자를 샀는지 안 샀는지 여부
    private var whetherHat1: Boolean = false // hat1을 사면 true
    private var whetherHat2: Boolean = false // hat2를 사면 true
    // 실제 GameFragment에서 쓸 모자. 둘 중 하나만 true로 만들어서 true인 모자를 씀
    private var wearHat1: Boolean = false
    private var wearHat2: Boolean = false

    // 모자
    private lateinit var hat: ImageView
    // 윌버가 자랄 때마다 모자의 위치 조절 위함 (모자는 윌버와 상대적 위치 - ConstraintLayout 사용
    private lateinit var hatLayout: ConstraintLayout.LayoutParams


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        marketButton.setOnClickListener {
            val intent = Intent(activity, GameMarketActivity::class.java)
            intent.putExtra("coin", coin) // 현재 coin 개수 넘기기
            intent.putExtra("pigsize", feed) // 현재 윌버가 얼마나 자랐는지
            intent.putExtra("hat1", whetherHat1) // hat1을 이미 가지고 있는지
            intent.putExtra("hat2", whetherHat2) // hat2를 이미 가지고 있는지
            startActivityForResult(intent, PHONE_ADD_REQUEST_CODE)
        }



        //친밀도 증가 부분 - 거미집 클릭
        webButton.setOnClickListener {

            shallot(view) // 샬롯이 거미줄을 늘려서 내려오는 애니메이션
            friend++ // 친밀도 1 증가

            if (friend == 1) {
                // 친밀도가 1일 때
                heart1.setImageResource(R.drawable.fullheart) // 하트 1 증가
                levelUpTextView.text = "대단한 돼지"
                showLevelUpMessage(view) // 대단한 돼지" 한글 알려줌
            } else if (friend == 2) {
                // 친밀도가 2일 때
                heart2.setImageResource(R.drawable.fullheart) // 하트 1 증가
                levelUpTextView.text = "멋진"
                showLevelUpMessage(view) // "멋진" 한글 알려줌
            } else if (friend == 3) {
                // 친밀도가 3일 때
                heart3.setImageResource(R.drawable.fullheart) // 하트 1 증가
                levelUpTextView.text = "🕷️ 우리는 친구 🐷"
                showLevelUpMessage(view) // 이제 절친임
            } else if (friend >= 4) {
                // 친밀도는 3이 최대임
                levelUpTextView.text = "이미 절친이에요 🤪"
                showLevelUpMessage(view)
            }
        }



        //윌버 키우기 부분 - 윌버 클릭
        pigButton.setOnClickListener {
            if (countText.text == "한 번 더?\n 스테이크를 눌러주세요 🥩" || countText.text == "한 번 더?\n 윌버를 눌러주세요 🐽") {
                restartGame(view) // 게임이 끝나 리셋함
            } else {
                feed++ // 밥 준 횟수 1씩 증가
                coin++ // 밥 줄 때마다 coin의 수도 1씩 증가
                howMuchCoin.text = "$coin coins" // coin 개수 띄우기
                countText.text = "윌버가 $feed" +"번 밥을 먹었어요!" // 밥 준 횟수 띄우기

                // 모자 쓴 상태 계속 유지해야함
                // 윌버가 자랄 때마다 모자의 위치도 바뀌어야 함 (버튼을 누를 때마다만 작동하면 됨)
                // 버튼 누르기 전에 마켓에서 산 걸 반영하는 건, onActivityResult에서 구현하였음
                setHat(wearHat1, wearHat2, feed)

                // 아기윌버-청소년윌버-성인윌버 별 세팅
                if (feed < 3) {
                    // 아기 윌버
                    bottle_front(view) // 밥 줄 때마다 젖병 움직임
                } else if (feed == 3) {
                    // 아기 윌버에서 청소년 윌버로 성장
                    levelUpTextView.text = "Level Up!"
                    showLevelUpMessage(view) // level up 글씨 나타내기

                    feedBottle.visibility = View.GONE // 젖병 없애고
                    feedSeed.visibility = View.VISIBLE // 씨앗 나타내기
                    feedBowl.visibility = View.VISIBLE // 씨앗통(사료통) 나타내기

                    // 아기 윌버를 청소년 윌버로 그림 변경하기
                    pigButton.setImageResource(R.drawable.middlepig) // 한 번 바뀌면 그 뒤는 안바뀜
                    pigButton.layoutParams.width = dp_to_px(middle) // 사이즈 키우기
                    pigButton.layoutParams.height = dp_to_px(middle) // 사이즈 키우기

                    moving_seed(view) // 청소년 윌버는 씨앗을 먹음
                } else if (feed > 3 && feed < 6) {
                    moving_seed(view) // 밥 줄 때마다 씨앗 움직임
                } else if (feed == 6) {
                    // 청소년 윌버에서 성인 윌버로 성장
                    levelUpTextView.text = "Level Up!"
                    showLevelUpMessage(view) // level up 글씨 나타내기

                    feedSeed.visibility = View.GONE // 씨앗 없애기
                    feedBowl.visibility = View.GONE // 씨앗통(사료통) 없애기
                    feedHayBowl.visibility = View.VISIBLE // 건초통 나타내기
                    feedHay.visibility = View.VISIBLE // 건초 나타내기

                    // 청소년 윌버를 성인 윌버로 그림 변경하기
                    pigButton.setImageResource(R.drawable.fattypig)
                    pigButton.layoutParams.width = dp_to_px(fatty) // 사이즈 조절
                    pigButton.layoutParams.height = dp_to_px(fatty) // 사이즈 조절

                    moving_hay(view) // 성인 윌버는 건초를 먹음
                } else if (feed > 6 && feed < 10) {
                    moving_hay(view) // 밥 줄 때마다 건초가 움직임
                } else if (feed >= 10) {
                    // 윌버가 끝까지 다 자랐을 때
                    feedHayBowl.visibility = View.GONE // 건초통 없애기
                    feedHay.visibility = View.GONE // 건초 없애기

                    webButton.isEnabled = false // 친밀도 증가시키는 샬롯의 거미줄 비활성화
                    pigButton.isEnabled = false // 윌버 버튼도 비활성화
                    marketButton.isEnabled = false // 윌버 버튼도 비활성화
                    
                    // 친밀도에 따라 바뀌는 결말
                    if (friend < 3) {
                        // 친밀도가 부족할 때 -> 스테이크로 변함
                        hat.visibility = View.INVISIBLE // 모자 숨기기
                        pigButton.setImageResource(R.drawable.steak) // 윌버가 스테이크로 변함
                        pigButton.layoutParams.width = dp_to_px(baby) // 사이즈 조절
                        pigButton.layoutParams.height = dp_to_px(baby) // 사이즈 조절
                        countText.text = "샬롯과 윌버가 친해지지 못했네요 😭\n결국 윌버는 주인 입으로...😢"
                        handler.postDelayed( { countText.text = "한 번 더?\n 스테이크를 눌러주세요 🥩" }, 2000)
                    } else {
                        // 친밀도가 충분하다면 윌버는 살아남음
                        friend1.visibility = View.VISIBLE // 눈 하트 뿅뿅
                        friend2.visibility = View.VISIBLE // 눈 하트 뿅뿅
                        countText.text = "샬롯이 윌버를 구해줬어요 👏\n샬롯과 윌버는 평생 행복하게 살았답니다 🤗"
                        handler.postDelayed( { countText.text = "한 번 더?\n 윌버를 눌러주세요 🐽" }, 2000)
                    }
                    // 결말 text가 나오는 동안 버튼 비활성화 상태였다가, 재시작 여부 물을 때 다시 버튼 활성화
                    handler.postDelayed( { pigButton.isEnabled = true }, 2000)
                }
            }
        }
        


        return view
    }


    
    // 마켓으로부터 받은 데이터들 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        // 데이터를 받아오면
        if (requestCode == PHONE_ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val newcoin = it.getIntExtra("remain", coin) // 모자를 사고 남은 코인
                val newhat = it.getIntExtra("hat", 0) // hat1과 hat2 중 무엇을 샀는지(혹은 둘 다 샀을 경우 무엇을 선택했는지)

                coin = newcoin // 원래 coin 변수에 newcoin 전달
                howMuchCoin.text = "$coin coins" // 코인 개수 보여주는 text 수정
                
                // 어떤 모자를 샀느냐에 따른 boolean 변수 수정
                if (newhat == 1) {
                    // hat1을 샀거나, 두 모자를 이미 다 샀을 경우 hat1을 선택했을 때
                    whetherHat1 = true // hat1을 샀음을 표시
                    wearHat1 = true // hat1을 선택했음을 표시
                    wearHat2 = false // hat2는 선택되지 않았음을 표시
                } else if (newhat == 2)  {
                    // hat2를 샀거나, 두 모자를 이미 다 샀을 경우 hat2를 선택했을 때
                    whetherHat2 = true // hat2를 샀음을 표시
                    wearHat1 = false // hat1은 선택되지 않았음을 표시
                    wearHat2 = true // hat2를 선택했음을 표시
                }
                // 금방 산 따끈따끈한 모자 씌우기
                setHat(wearHat1, wearHat2, feed)
            }
        }

    }
    
    
    
    // 레벨업 했을 때 글씨 띄우기 및 친밀도 1씩 증가할 때 글씨 띄우기
    private fun showLevelUpMessage(view: View) {
        levelUpTextView.visibility = View.VISIBLE // levelupTextView 보이게 만들기
        handler.postDelayed( { levelUpTextView.visibility = View.INVISIBLE }, 1500) // 1.5초만 띄우기
    }
    
    
    
    // 다시 시작할 때 초기 세팅으로 바꿔주기
    private fun restartGame(view: View) {
        pigButton.setImageResource(R.drawable.littlepig) // 아기 윌버로 버튼 이미지 다시 변경해주기
        pigButton.layoutParams.width = dp_to_px(baby) // 아기 윌버 사이즈로 다시 변경
        pigButton.layoutParams.height = dp_to_px(baby)

        hat.visibility = View.INVISIBLE // 모자 지우기
        hat.layoutParams.width = dp_to_px(100f) // 모자 사이즈도 초기화
        hat.layoutParams.height = dp_to_px(100f)
        
        // 친밀도 하트 다시 비우기
        heart1.setImageResource(R.drawable.emptyheart)
        heart2.setImageResource(R.drawable.emptyheart)
        heart3.setImageResource(R.drawable.emptyheart)
        
        // 윌버 눈 위 하트 뿅뿅 지우기
        friend1.visibility = View.INVISIBLE
        friend2.visibility = View.INVISIBLE

        hat.visibility = View.INVISIBLE // 모자 지우기

        webButton.isEnabled = true // 친밀도 버튼(샬롯의 거미줄) 다시 활성화시키기
        marketButton.isEnabled = true // 마켓 버튼도 다시 활성화시키기
        whetherHat1 = false // hat1 구매 여부 초기화
        whetherHat2 = false // hat2 구매 여부 초기화
        wearHat1 = false // hat1 쓰고 있는지 여부 초기화
        wearHat2 = false // hat2 쓰고 있는지 여부 초기화

        feed = 0 // 밥 준 횟수 초기화
        friend = 0 // 친밀도 숫자 초기화
        coin = 0 // coin 수 초기화
        howMuchCoin.text = "$coin coins" // 초기화한 뒤 다시 coin 개수 나타내기
        countText.text = "윌버가 $feed" +"번 밥을 먹었어요!" // 초기화한 뒤 다시 먹이 준 개수 나타내기
    }
    
    
    
    // 애니메이션 모음집
    // 아기 윌버의 젖병 움직이기
    public fun bottle_front(view: View) {
        // 원래 위치에서 오른쪽 대각선 위, 아기 윌버의 코에 가까워지게 움직임
        val babybottleanim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_babybottle)
        feedBottle.startAnimation(babybottleanim)
    }

    // 청소년 윌버의 씨앗 움직이기
    public fun moving_seed(view: View) {
        // 원래 위치에서 왼쪽 대각선 위, 청소년 윌버의 코에 가까워지게 움직임
        val movingseedanim: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_moveseed)
        feedSeed.startAnimation(movingseedanim)
    }

    // 성인 윌버의 건초 움직이기
    public fun moving_hay(view: View) {
        // 원래 위치에서 오른쪽 대각선 위, 성인 윌버의 코에 가까워지게 움직임
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

    
    
    // dp 단위를 px로 바꿔주기 - 위치 조정을 위해서
    public fun dp_to_px(dpsize: Float): Int {
        val pxsize: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, resources.displayMetrics)
        return Math.round(pxsize)
    }

    
    
    // 윌버의 크기에 따라 모자 씌우기
    public fun make_hat_fit(start: Float, top: Float) {
        // start와 top의 값이 주어졌을 때, 그 값에 모자를 위치시키는 역할
        hatLayout.marginStart = dp_to_px(start) // start 부분으로부터의 margin 세팅
        hatLayout.topMargin = dp_to_px(top) // top 부분으로부터의 margin 세팅
        hat.layoutParams = hatLayout // 변경된 layout을 hat에게 세팅
        hat.visibility = View.VISIBLE // 숨겨져 있던 모자를 보이게 함
    }
    public fun setHat(wearHat1: Boolean, wearHat2: Boolean, feed: Int) {
        // 윌버의 크기에 따라 다른 모자의 위치 - 이를 세팅하기 위한 함수
        if (wearHat1 && !wearHat2) {
            hat.setImageResource(R.drawable.hat1) // hat1 쓸 때
            if (feed < 3) {
                make_hat_fit(55f, -20f) // 아기윌버, 200dp, 기준값
            } else if (feed < 6) {
                setMiddleHat() // 250/200의 값만큼 모두 곱해줌
                make_hat_fit(131.25f, -22.5f) // 청소년 윌버, 250dp
            } else if (feed <= 10) {
                setFattyHat() // 300/200의 값만큼 모두 곱해줌
                make_hat_fit(75f, -67.5f) // 성인 윌버, 300dp
            }
        } else if (!wearHat1 && wearHat2) {
            hat.setImageResource(R.drawable.hat2) // hat2 쓸 때
            if (feed < 3) {
                make_hat_fit(45f, -40f)
            } else if (feed < 6) {
                setMiddleHat()
                make_hat_fit(118.75f, -43.75f)
            } else if (feed <= 10) {
                setFattyHat()
                make_hat_fit(67.5f, -101f) // 원래는 -105f. 살짝 내리니까 더 괜찮아서 이 값으로 설정
            }
        }
    }
    public fun setMiddleHat() {
        // 250/200의 비율만큼 모자의 크기도 증가
        val middleHat: Float = 100f*(250f/200f)
        hat.layoutParams.width = dp_to_px(middleHat)
        hat.layoutParams.height = dp_to_px(middleHat)
    }
    public fun setFattyHat() {
        // 300/200의 비율만큼 모자의 크기도 증가
        val FattyHat: Float = 100f*(300f/200f)
        hat.layoutParams.width = dp_to_px(FattyHat)
        hat.layoutParams.height = dp_to_px(FattyHat)
    }

    companion object {
        private const val PHONE_ADD_REQUEST_CODE = 123 // Intent할 때 사용
        private const val READ_CONTACTS_REQUEST_CODE = 1 // Intent할 때 사용
    }

}