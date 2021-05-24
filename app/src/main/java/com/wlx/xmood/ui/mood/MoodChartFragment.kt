package com.wlx.xmood.ui.mood

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wlx.xmood.R
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MoodChartFragment(private val timeType: Int) : Fragment() {

    companion object {
        fun newInstance(timeType: Int) = MoodChartFragment(timeType)
        val HOUR_TYPE = 0
        val DAY_TYPE = 1
        val WEEK_TYPE = 2
        val MONTH_TYPE = 3
    }

    private lateinit var viewModel: MoodChartViewModel
    private lateinit var root: View
    private lateinit var text: TextView

    private lateinit var lineChartView: LineChartView
    private lateinit var lineChartData: LineChartData
    private val numOfLines = 1
    private val maxNumOfLines = 4
    private val numOfPoints = 12

    var randomNumbersTab = Array(maxNumOfLines) {
        FloatArray(
            numOfPoints
        )
    }

    private val hasAxes = true
    private val hasAxesNames = true
    private val hasLines = true
    private val hasPoints = true
    private val shape = ValueShape.CIRCLE
    private val isFilled = false  //是否对线的下方进行填充
    private val hasLabels = false  //是否显示标签
    private val isCubic = true  //是否是平滑曲线
    private val hasLabelForSelected = false
    private val pointsHaveDifferentColor = false
    private val hasGradientToTransparent = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.mood_chart_fragment, container, false)

        lineChartView = root.findViewById(R.id.mood_linechart)

        lineChartView.onValueTouchListener = ValueTouchListener()

        generateValues()
        generateData()

        // Disable viewport recalculations, see toggleCubic() method for more info.
        lineChartView.isViewportCalculationEnabled = false

        resetViewport()

//        text = root.findViewById(R.id.mood_chart_test_text)
        return root
    }



    private fun generateValues() {
        for (i in 0 until maxNumOfLines) {
            for (j in 0 until numOfPoints) {
                randomNumbersTab[i][j] = (Math.random() * 5f + 1).roundToInt().toFloat()
            }
        }
    }

    private fun generateData() {
        val lines: MutableList<Line> = ArrayList()
        for (i in 0 until numOfLines) {
            val values: MutableList<PointValue> = ArrayList()
            for (j in 0 until numOfPoints) {
                values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
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
                line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
            }
            lines.add(line)
        }
        lineChartData = LineChartData(lines)
        val time = Date()
        val rightnow = Calendar.getInstance()
        rightnow.time = time
        rightnow.add(Calendar.DAY_OF_YEAR, -1)
        val sdf = SimpleDateFormat("MM-dd")
        val mAxisXValues: MutableList<AxisValue> = ArrayList()
        for (i in 0..11) {
            rightnow.add(Calendar.DAY_OF_YEAR, 1)
            var axisValue = AxisValue(i.toFloat())
            mAxisXValues.add(axisValue.setLabel(sdf.format(rightnow.time)))
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

    private fun resetViewport() {
        // Reset viewport height range to [1,6]
        val v = Viewport(lineChartView.maximumViewport)
        v.bottom = 0f
        v.top = 7f
        v.left = 1f
        v.right = (numOfPoints - 1).toFloat()
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

        }

        override fun onValueDeselected() {

        }
    }

}