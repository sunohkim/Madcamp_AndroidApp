package com.example.madcamp_androidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ImageView
import android.util.Log
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.util.TypedValue
import java.lang.Math


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
        pigButton = view.findViewById(R.id.btn_little_pig)
        countText = view.findViewById(R.id.tv_count)
        levelUpTextView = view.findViewById(R.id.tv_level_up)
        feedBottle = view.findViewById(R.id.feed_bottle)
        webLine = view.findViewById(R.id.line)
        spider = view.findViewById(R.id.spider)
        webButton = view.findViewById(R.id.btn_web)
        friendText = view.findViewById(R.id.tv_friend)
        feedBowl = view.findViewById(R.id.feed_bowl)
        feedSeed = view.findViewById(R.id.seed)
        feedHayBowl = view.findViewById(R.id.feed_hay)
        feedHay = view.findViewById(R.id.hay)
        heart1 = view.findViewById(R.id.heart1)
        heart2 = view.findViewById(R.id.heart2)
        heart3 = view.findViewById(R.id.heart3)
        friend1 = view.findViewById(R.id.be_friends1)
        friend2 = view.findViewById(R.id.be_friends2)

        
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


        //윌버 키우기 부분 - 윌버 클릭
        pigButton.setOnClickListener {
            if (countText.text == "한 번 더?\n 스테이크를 눌러주세요 🥩" || countText.text == "한 번 더?\n 윌버를 눌러주세요 🐽") {
                
                // 리셋할 때
                restartGame(view)

            } else {

                feed++ // 밥 준 횟수 1씩 증가
                //Log.w("feed: ", feed.toString())
                countText.text = "윌버가 $feed" +"번 밥을 먹었어요!"

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
                    
                    if (friend < 3) {
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
                    handler.postDelayed( {pigButton.isEnabled = true }, 2000)
                }

            }
        }

        return view
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
        
        // 밥 준 횟수 및 친밀도 숫자 초기화
        feed = 0
        friend = 0
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
}