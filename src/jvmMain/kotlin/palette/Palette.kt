package palette

import java.awt.Color
import java.awt.image.BufferedImage

object Palette {
    /**
     * 获取图片主色调的rgb值
     * @return
     * @throws Exception
     */
    fun generate(image: BufferedImage): RGB {
        val hueCountMap: MutableMap<Float, Int> =
            HashMap()
        val hslCountMap: MutableMap<HSL, Int> = HashMap()
        val width = image.width
        val height = image.height
        val minx = image.minX
        val miny = image.minY
        //计算各点的hsl值，并统计数量
        for (i in minx until width) {
            for (j in miny until height) {
                val pixel = image.getRGB(i, j)
                val color = Color(pixel)
                val rgb = RGB(color.red, color.green, color.blue)
                val hsl = ColorConverter.RGB2HSL(rgb)!!
                val h = computeHue(hsl.h1).toFloat()
                val s = computeSAndL(hsl.s1).toFloat()
                val l = computeSAndL(hsl.l1).toFloat()
                val newHSL = HSL(h, s, l)
                //统计hue值数量
                var count = hueCountMap[h]
                if (count == null) {
                    hueCountMap[h] = 1
                } else {
                    hueCountMap[h] = count + 1
                }
                //统计HSL数量
                count = hslCountMap[newHSL]
                if (count == null) {
                    hslCountMap[newHSL] = 1
                } else {
                    hslCountMap[newHSL] = count + 1
                }
            }
        }
        //查找数量最多的hue值
        var maxHue = 0f
        var maxCount = 0
        for ((hue, count) in hueCountMap) {
            if (count > maxCount) {
                maxCount = count
                maxHue = hue
            }
        }
        //查找maxHue中数量最多的hsl值
        var maxHSL: HSL? = null
        maxCount = 0
        for ((hsl, count) in hslCountMap) {
            if (hsl.h1 == maxHue && count > maxCount) {
                maxCount = count
                maxHSL = hsl
            }
        }
        //hsl转rgb
        return ColorConverter.HSL2RGB(maxHSL)!!
    }

    /**
     * 按格子划分h值
     * @param h
     * @return
     */
    fun computeHue(h: Float): Int {
        if (h <= 15) {
            return 0
        }
        if (15 < h && h <= 45) {
            return 30
        }
        if (45 < h && h <= 75) {
            return 60
        }
        if (75 < h && h <= 105) {
            return 90
        }
        if (105 < h && h <= 135) {
            return 120
        }
        if (135 < h && h <= 165) {
            return 150
        }
        if (165 < h && h <= 195) {
            return 180
        }
        if (195 < h && h <= 225) {
            return 210
        }
        if (225 < h && h <= 255) {
            return 240
        }
        if (255 < h && h <= 285) {
            return 270
        }
        if (285 < h && h <= 315) {
            return 300
        }
        if (315 < h && h <= 345) {
            return 330
        }
        return if (345 < h) {
            360
        } else 360
    }

    /**
     * 按格子划分s和l值
     * @param s
     * @return
     */
    fun computeSAndL(s: Float): Int {
        if (s <= 32) {
            return 0
        }
        if (32 < s && s <= 96) {
            return 64
        }
        if (96 < s && s <= 160) {
            return 128
        }
        if (160 < s && s <= 224) {
            return 192
        }
        return if (s > 224) {
            255
        } else 255
    }
}