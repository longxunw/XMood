package com.wlx.xmood.ui.mood

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wlx.xmood.R
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import kotlin.collections.ArrayList

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
    private lateinit var moodChartViewModel: MoodChartViewModel
    private lateinit var root: View
    private lateinit var text: TextView

    private lateinit var lineChartView: LineChartView
    private lateinit var lineChartData: LineChartData

    private lateinit var cardDateText : TextView //卡片内容---日期
    private lateinit var progressBar: ProgressBar //卡片内容---进度条
    private lateinit var cRating: TextView //卡片内容---rating值
    private lateinit var cDescription: TextView //卡片内容---事情描述

    private val hasAxes = true
    private val hasAxesNames = true
    private val hasLines = true
    private val hasPoints = true
    private val shape = ValueShape.CIRCLE
    private val isFilled = false  //是否对线的下方进行填充
    private val hasLabels = false  //是否显示标签
    private val isCubic = false  //是否是平滑曲线
    private val hasLabelForSelected = false
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
        cardDateText = root.findViewById(R.id.mood_detail_date)
        progressBar = root.findViewById(R.id.rating_progressbar)
        cRating = root.findViewById(R.id.rating_in_details)
        cDescription = root.findViewById(R.id.description_in_details)

//        when(timeType){
//            HOUR_TYPE->{
//                moodChartViewModel.searchNode(HOUR_TYPE)
//            }
//            DAY_TYPE->{
//                moodChartViewModel.searchNode(DAY_TYPE)
//            }
//            WEEK_TYPE->{
//                moodChartViewModel.searchNode(WEEK_TYPE)
//            }
//            MONTH_TYPE->{
//                moodChartViewModel.searchNode(MONTH_TYPE)
//            }
//        }

        moodChartViewModel.nodeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val nodes = result.getOrNull()
            if (nodes != null) {
                moodChartViewModel.nodeList.clear()
                for (node in nodes) {
                    moodChartViewModel.nodeList.add(node)
                    moodChartViewModel.nodeRateList.add(node.rating)
                }
                drawLineChart()
                resetViewport()
                initCard()
            }
        })

        // Disable viewport recalculations, see toggleCubic() method for more info.
        lineChartView.isViewportCalculationEnabled = false

//        text = root.findViewById(R.id.mood_chart_test_text)

        return root
    }

    private fun drawLineChart() {
        val lines: MutableList<Line> = ArrayList()
        val values: MutableList<PointValue> = ArrayList()

//        val startIndex = if(moodChartViewModel.nodeList.size >= 6){
//            moodChartViewModel.nodeList.lastIndex -5
//        } else{ 0 }  //只显示最近的几个节点

        //Y轴 values
        for(i in 0 until moodChartViewModel.nodeList.size){
            values.add(PointValue(i.toFloat(),moodChartViewModel.nodeRateList[i].toFloat()))
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
        line.setHasLabelsOnlyForSelected(true)
        if (pointsHaveDifferentColor) {
            line.pointColor = ChartUtils.COLORS[(lines.size + 1) % ChartUtils.COLORS.size]
        }
        lines.add(line)
        lineChartData = LineChartData(lines)

        //X轴 values
        val mAxisXValues: MutableList<AxisValue> = ArrayList()
        for (i in 0 until moodChartViewModel.nodeList.size){
            var axisValue = AxisValue(i.toFloat())

//            mAxisXValues.add(axisValue.setLabel("1月1日"))
//            Log.d("timepicker", TimeUtil.Long2Str(moodChartViewModel.nodeList[i].date,"yyyy-MM-dd") )

            var loc_calendar : Calendar = Calendar.getInstance()
            loc_calendar.timeInMillis = moodChartViewModel.nodeList[i].date
            var month = loc_calendar.get(Calendar.MONTH) + 1
            var day = loc_calendar.get(Calendar.DAY_OF_MONTH)
//            Log.d("added date: ","$month.$day")
            mAxisXValues.add(axisValue.setLabel("$month.$day"))
        }

        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Date"
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
        lineChartView.zoomType = ZoomType.HORIZONTAL
        lineChartView.lineChartData = lineChartData
    }

    private fun initCard() {
        //获取最近时间的一个节点
        val recentNode = moodChartViewModel.nodeList[moodChartViewModel.nodeList.lastIndex]

//        Log.d("size: ", moodChartViewModel.nodeList.size.toString() )
//        Log.d("lastindex: ", moodChartViewModel.nodeList.lastIndex.toString() )

//        初始化card的时间
        val date = recentNode.date
        val localCalendar : Calendar = Calendar.getInstance()
        localCalendar.timeInMillis = date
        val month = localCalendar.get(Calendar.MONTH) + 1
        val day = localCalendar.get(Calendar.DAY_OF_MONTH)
        val str = StringBuilder()
        str.append(month.toString()).append("月").append(day.toString()).append("日")
        cardDateText.text = str.toString()
        //初始化进度条
        progressBar.progress = recentNode.rating
        //初始化rating数值
        cRating.text = recentNode.rating.toString()
        //初始化event具体描述
        cDescription.text = recentNode.event

    }

    override fun onResume() {
        super.onResume()
        when(timeType){
            HOUR_TYPE->{
                moodChartViewModel.searchNode(HOUR_TYPE)
            }
            DAY_TYPE->{
                moodChartViewModel.searchNode(DAY_TYPE)
            }
            WEEK_TYPE->{
                moodChartViewModel.searchNode(WEEK_TYPE)
            }
            MONTH_TYPE->{
                moodChartViewModel.searchNode(MONTH_TYPE)
            }
        }
    }

    private fun resetViewport() {
        // Reset viewport height range to [1,6]
        val v = Viewport(lineChartView.maximumViewport)
        v.bottom = 0f
        v.top = 7f
        v.left = 0f
        v.right = moodChartViewModel.nodeList.size.toFloat()

        lineChartView.maximumViewport = v
        lineChartView.currentViewport = v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MoodChartViewModel::class.java)
//        when (timeType) {
//            0 -> text.text = "this is Hour Chart"
//            1 -> text.text = "this is Day Chart"
//            2 -> text.text = "this is Week Chart"
//            3 -> text.text = "this is Month Chart"
//            else -> text.text = "this is Hour Chart"
//        }
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

//            Log.d("pointIndex: ",pointIndex.toString())
//            Log.d("date:",moodChartViewModel.nodeList[pointIndex].date.toString())
//            Log.d("day in date: ", day.toString())
//            Log.d("month in date: ", month.toString())
//            Log.d("rating: ",moodChartViewModel.nodeRateList[pointIndex].toString())

//            cardDateText.text = month.toString() + "月" + day.toString() + "日"
            val str = StringBuilder()
            str.append(month.toString()).append("月").append(day.toString()).append("日")
            cardDateText.text = str.toString()

            progressBar.progress = moodChartViewModel.nodeList[pointIndex].rating
            //修改rating数值
            cRating.text = moodChartViewModel.nodeList[pointIndex].rating.toString()
            //修改event具体描述
            cDescription.text = moodChartViewModel.nodeList[pointIndex].event
        }

        override fun onValueDeselected() {

        }
    }

}