package com.wlx.xmood.ui.mood

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.R
import com.wlx.xmood.ui.mood.edit.MoodEditActivity
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class MoodChartFragment(private val timeType: Int) : Fragment() {

    // private val view: View
    companion object {
        fun newInstance(timeType: Int) = MoodChartFragment(timeType)
        val HOUR_TYPE = 0
        val DAY_TYPE = 1
        val WEEK_TYPE = 2
        val MONTH_TYPE = 3
    }
    constructor() : this(HOUR_TYPE)
    private val TAG ="MoodCharFragment"
    private lateinit var moodChartViewModel: MoodChartViewModel
    private lateinit var root: View
    private lateinit var text: TextView
    private var mid = -1

    private lateinit var lineChartView: LineChartView
    private lateinit var lineChartData: LineChartData

    private lateinit var moodCard : CardView
    private lateinit var cardDateText : TextView //卡片内容---日期
    private lateinit var progressBar: ProgressBar //卡片内容---进度条
    private lateinit var cRating: TextView //卡片内容---rating值
    private lateinit var cDescription: TextView //卡片内容---事情描述
    private lateinit var cDetailTitle: TextView //卡片Detail

    private val hasAxes = true
    private val hasAxesNames = true
    private val hasLines = true
    private val hasPoints = true
    private val shape = ValueShape.CIRCLE
    private val isFilled = false  //是否对线的下方进行填充
    private val hasLabels = false  //是否显示标签
    private val isCubic = false  //是否是平滑曲线
    private val hasLabelForSelected = true
    private val pointsHaveDifferentColor = false
    private val hasGradientToTransparent = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //绑定ViewModel
        moodChartViewModel =
            ViewModelProvider(this).get(MoodChartViewModel::class.java)

        //初始化root
        root = inflater.inflate(R.layout.mood_chart_fragment, container, false)

        //初始化折线图
        lineChartView = root.findViewById(R.id.mood_linechart)
        lineChartView.onValueTouchListener = ValueTouchListener()

        //初始化Card
        moodCard = root.findViewById(R.id.mood_landing_card)
        cardDateText = root.findViewById(R.id.mood_detail_date)
        progressBar = root.findViewById(R.id.rating_progressbar)
        cRating = root.findViewById(R.id.rating_in_details)
        cDescription = root.findViewById(R.id.description_in_details)
        cDetailTitle = root.findViewById(R.id.detail_to_edit)

        moodChartViewModel.nodeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val nodes = result.getOrNull()
            if (nodes != null) {
                moodChartViewModel.nodeList.clear()
                for (node in nodes) {
                    moodChartViewModel.nodeList.add(node)
                }
                drawLineChart()
                resetViewport(timeType)
                initCard()

                // Disable viewport recalculations, see toggleCubic() method for more info.
                lineChartView.isViewportCalculationEnabled = false
                lineChartView.isScrollEnabled = true

            }
        })

        return root
    }

    private fun drawLineChart() {
        val lines: MutableList<Line> = ArrayList()
        val values: MutableList<PointValue> = ArrayList()

        //Y轴 values
        for(i in 0 until moodChartViewModel.nodeList.size){

//            val current = LocalDateTime.now()
            val current : Calendar = Calendar.getInstance()

            val loc_calendar : Calendar = Calendar.getInstance()
            loc_calendar.timeInMillis = moodChartViewModel.nodeList[i].date
            val month = loc_calendar.get(Calendar.MONTH) + 1
            val week = loc_calendar.get(Calendar.DAY_OF_WEEK)
            val day = loc_calendar.get(Calendar.DAY_OF_MONTH)
            val hour = loc_calendar.get(Calendar.HOUR_OF_DAY)
            val minute = loc_calendar.get(Calendar.MINUTE)

            Log.d("loc_calendar: ", "$month $week $day $hour $minute")

            when(timeType){
                HOUR_TYPE->{
                    if(current.get(Calendar.DAY_OF_MONTH) == day
                        && current.get(Calendar.MONTH) + 1 == month
                        && current.get(Calendar.HOUR_OF_DAY) == hour) {
                            val x = minute.toFloat()
                            values.add(PointValue(x, moodChartViewModel.nodeList[i].rating.toFloat()))
                    }
                }
                DAY_TYPE->{
                    if(current.get(Calendar.DAY_OF_MONTH) == day
                        && current.get(Calendar.MONTH) + 1 == month) {
                        val x = hour.toFloat()*60 + minute.toFloat()
                        values.add(PointValue(x, moodChartViewModel.nodeList[i].rating.toFloat()))
                    }
                }
                WEEK_TYPE->{
                    if(current.get(Calendar.WEEK_OF_MONTH)  == loc_calendar.get(Calendar.WEEK_OF_MONTH)
                        && current.get(Calendar.MONTH) + 1 == month) {
                        val x = week.toFloat()*24*60 + hour.toFloat()*60 + minute.toFloat()
                        values.add(PointValue(x, moodChartViewModel.nodeList[i].rating.toFloat()))
                    }
                }
                MONTH_TYPE->{
                    if(current.get(Calendar.MONTH) + 1 == month) {
                        val x = day.toFloat()*24*60 + hour.toFloat()*60 + minute.toFloat()
                        values.add(PointValue(x ,moodChartViewModel.nodeList[i].rating.toFloat()))
                    }
                }
            }
        }

        val line = Line(values)
        line.color = Color.parseColor("#EF8354")
        line.pointColor = Color.parseColor("#EF8354")
        line.shape = shape
        line.isCubic = isCubic
        line.isFilled = isFilled
        line.setHasLabels(hasLabels)
        line.setHasLabelsOnlyForSelected(hasLabelForSelected)
        line.setHasLines(hasLines)
        line.setHasPoints(hasPoints)
        line.pointRadius = 3
        line.strokeWidth = 2
//        line.setHasLabelsOnlyForSelected(true)
        if (pointsHaveDifferentColor) {
            line.pointColor = ChartUtils.COLORS[(lines.size + 1) % ChartUtils.COLORS.size]
        }
        lines.add(line)

        lineChartData = LineChartData(lines)

        //X轴 values
        val mAxisXValues: MutableList<AxisValue> = ArrayList()
        when(timeType){
            HOUR_TYPE->{
                for (i in 0 until 60){
                    val axisValue = AxisValue(i.toFloat())
                    mAxisXValues.add(axisValue.setLabel(i.toString()))
                }
            }
            DAY_TYPE->{
                for (i in 0 until 1440){
                    val axisValue = AxisValue(i.toFloat())
                    mAxisXValues.add(axisValue.setLabel((i/60).toString()))
                }
            }
            WEEK_TYPE->{
                for (i in 0 until 10080){
                    val axisValue = AxisValue(i.toFloat())
                    mAxisXValues.add(axisValue.setLabel((i/1440).toString()))
                }
            }
            MONTH_TYPE->{
                for (i in 0 until 302400){
                    val axisValue = AxisValue(i.toFloat())
                    mAxisXValues.add(axisValue.setLabel((i/10080).toString()))
                }
            }
        }

        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                when(timeType){
                    HOUR_TYPE->{
                        axisX.name = "Hour"
                        axisX.maxLabelChars = 7 //相当于设置X轴刻度
                    }
                    DAY_TYPE->{
                        axisX.name = "Day"
                        axisX.maxLabelChars = 6 //相当于设置X轴刻度
                    }
                    WEEK_TYPE->{
                        axisX.name = "Week"
                        axisX.maxLabelChars = 7 //相当于设置X轴刻度
                    }
                    MONTH_TYPE->{
                        axisX.name = "Month"
                        axisX.maxLabelChars = 6 //相当于设置X轴刻度
                    }
                }
                axisY.name = "Rating"
            }
            axisX.values = mAxisXValues
            axisX.setHasTiltedLabels(false)
            lineChartData.axisXBottom = axisX
            lineChartData.axisYLeft = axisY
        } else {
            lineChartData.axisXBottom = null
            lineChartData.axisYLeft = null
        }
        lineChartData.valueLabelBackgroundColor = Color.TRANSPARENT
        lineChartData.isValueLabelBackgroundAuto = true
        lineChartData.isValueLabelBackgroundEnabled = true
        lineChartData.valueLabelTextSize = 6
        lineChartData.setValueLabelsTextColor(Color.WHITE)
        lineChartData.baseValue = Float.NEGATIVE_INFINITY
        lineChartView.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
        lineChartView.lineChartData = lineChartData
    }

    private fun initCard() {
        //获取最近时间的一个节点
        val recentNodeIndex = moodChartViewModel.nodeList.lastIndex
        if(recentNodeIndex < 0){
            cardDateText.text = ""
            progressBar.progress = 0
            cRating.text = "0"
            cDescription.text = "Nothing! Add one!"
            return
        }

        val recentNode = moodChartViewModel.nodeList[moodChartViewModel.nodeList.lastIndex]

        mid = recentNode.id

//        初始化card的时间
        val date = recentNode.date
        val localCalendar : Calendar = Calendar.getInstance()
        localCalendar.timeInMillis = date
        val month = localCalendar.get(Calendar.MONTH) + 1
        val day = localCalendar.get(Calendar.DAY_OF_MONTH)
        val hour = localCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = localCalendar.get(Calendar.MINUTE)
        val str = StringBuilder()
        str.append(month.toString()).append("月").append(day.toString()).append("日")
            .append(hour.toString()).append("时").append(minute.toString()).append("分")
        cardDateText.text = str.toString()
        //初始化进度条
        progressBar.progress = recentNode.rating
        //初始化rating数值
        cRating.text = recentNode.rating.toString()
        //初始化event具体描述
        cDescription.text = recentNode.event
        //初始化Detail进入编辑界面事件
        moodCard.setOnClickListener {
            val intent = Intent(context, MoodEditActivity::class.java)
            intent.putExtra("id", mid)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context?.startActivity(intent);
        }
    }

    override fun onResume() {
        super.onResume()
        val current : Calendar = Calendar.getInstance()

        when(timeType){
            HOUR_TYPE->{
                current.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY))
                current.set(Calendar.MINUTE,0)
                current.set(Calendar.SECOND,0)
                current.set(Calendar.MILLISECOND,0)  //设置到 当前h0min0s

                val startTime = current.timeInMillis
                current.add(Calendar.HOUR, 1) //设置到 下一h0min0s
                val endTime = current.timeInMillis
                moodChartViewModel.searchNode(startTime, endTime)
            }

            DAY_TYPE->{
                current.set(Calendar.HOUR_OF_DAY, 0)
                current.set(Calendar.MINUTE,0)
                current.set(Calendar.SECOND,0)
                current.set(Calendar.MILLISECOND,0)  //设置到 今天当前h0min0s

                val startTime = current.timeInMillis
                current.set(Calendar.HOUR_OF_DAY, 23)
                current.set(Calendar.MINUTE, 59)  //设置到 今天29h59min0s
                val endTime = current.timeInMillis
                moodChartViewModel.searchNode(startTime, endTime)
            }
            WEEK_TYPE->{
                current.set(Calendar.DAY_OF_WEEK, 1)
                current.set(Calendar.HOUR_OF_DAY, 0)
                current.set(Calendar.MINUTE, 0)
                current.set(Calendar.SECOND, 0)
                current.set(Calendar.MILLISECOND, 0)  //设置到 本周第一天0h0min0s

                val startTime = current.timeInMillis
                current.set(Calendar.DAY_OF_WEEK,7)
                current.set(Calendar.HOUR_OF_DAY, 23)
                current.set(Calendar.MINUTE, 59) //设置到 本周第7天23h59min0s
                val endTime = current.timeInMillis
                moodChartViewModel.searchNode(startTime, endTime)
            }
            MONTH_TYPE->{
                current.set(Calendar.DAY_OF_MONTH, 1)
                current.set(Calendar.HOUR_OF_DAY, 0)
                current.set(Calendar.MINUTE, 0)
                current.set(Calendar.SECOND, 0)
                current.set(Calendar.MILLISECOND, 0)  //设置到 本月第一天0h0min0s

                val startTime = current.timeInMillis
                current.add(Calendar.MONTH, 1)
                current.add(Calendar.MINUTE, -1) //设置到 下月第一天0h0min0s 减去一分钟
                val endTime = current.timeInMillis
                moodChartViewModel.searchNode(startTime, endTime)
            }
        }
    }

    private fun resetViewport(timeType: Int) {
        // Reset viewport height range to [1,6]
        val v = Viewport(lineChartView.maximumViewport)
        v.bottom = 0f
        v.top = 7f
        v.left = 0f
        when(timeType){
            HOUR_TYPE->{
                v.right = 61f
            }
            DAY_TYPE->{
                v.right = 1440f
            }
            WEEK_TYPE->{
                v.right = 10080f
            }
            MONTH_TYPE->{
                v.right = 302400f
            }
        }
//        v.right = moodChartViewModel.nodeList.size.toFloat()-1
//        v.right = 60f
        lineChartView.maximumViewport = v
        lineChartView.currentViewport = v
        lineChartView.isScrollContainer = true
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private inner class ValueTouchListener : LineChartOnValueSelectListener {
        override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {

            //更新卡片的内容
//            cardDateText = root.findViewById(R.id.mood_detail_date)
//            cardDateText.text = moodChartViewModel.nodeList[pointIndex].date.toString()
            val localCalendar : Calendar = Calendar.getInstance()
            localCalendar.timeInMillis = moodChartViewModel.nodeList[pointIndex].date

            val month = localCalendar.get(Calendar.MONTH) + 1
            val day = localCalendar.get(Calendar.DAY_OF_MONTH)
            val hour = localCalendar.get(Calendar.HOUR_OF_DAY)
            val minute = localCalendar.get(Calendar.MINUTE)

            val str = StringBuilder()
            str.append(month.toString()).append("月").append(day.toString()).append("日")
                .append(hour.toString()).append("时").append(minute.toString()).append("分")
            cardDateText.text = str.toString()


            progressBar.progress = moodChartViewModel.nodeList[pointIndex].rating
            //修改rating数值
            cRating.text = moodChartViewModel.nodeList[pointIndex].rating.toString()
            //修改event具体描述
            cDescription.text = moodChartViewModel.nodeList[pointIndex].event
            Log.d(TAG, "onValueSelected: ${moodChartViewModel.nodeList[pointIndex].id}")
            mid = moodChartViewModel.nodeList[pointIndex].id
//            moodCard.setOnClickListener {
//                val intent = Intent(context, MoodEditActivity::class.java)
//                intent.putExtra("id", mid)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context?.startActivity(intent)
//            }
        }

        override fun onValueDeselected() {

        }
    }

}