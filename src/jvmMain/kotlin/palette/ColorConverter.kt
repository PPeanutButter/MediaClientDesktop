package palette

object ColorConverter {
    /**
     * @param rgb
     * @return
     */
    fun RGB2HSL(rgb: RGB?): HSL? {
        if (rgb == null) {
            return null
        }
        val red = rgb.getRed()
        val green = rgb.getGreen()
        val blue = rgb.getBlue()
        val var_Min = Math.min(red, Math.min(blue, green)).toFloat()
        val var_Max = Math.max(red, Math.max(blue, green)).toFloat()
        val del_Max = var_Max - var_Min
        var H = 0f
        var S = 0f
        var L = (var_Max + var_Min) / 2
        if (del_Max == 0f) {
            H = 0f
            S = 0f
        } else {
            S = if (L < 128) {
                256 * del_Max / (var_Max + var_Min)
            } else {
                256 * del_Max / (512 - var_Max - var_Min)
            }
            val del_R = ((360 * (var_Max - red) / 6 + 360 * del_Max / 2)
                    / del_Max)
            val del_G = ((360 * (var_Max - green) / 6 + 360 * del_Max / 2)
                    / del_Max)
            val del_B = ((360 * (var_Max - blue) / 6 + 360 * del_Max / 2)
                    / del_Max)
            if (red.toFloat() == var_Max) {
                H = del_B - del_G
            } else if (green.toFloat() == var_Max) {
                H = 120 + del_R - del_B
            } else if (blue.toFloat() == var_Max) {
                H = 240 + del_G - del_R
            }
            if (H < 0) {
                H += 360f
            }
            if (H >= 360) {
                H -= 360f
            }
            if (L >= 256) {
                L = 255f
            }
            if (S >= 256) {
                S = 255f
            }
        }
        return HSL(H, S, L)
    }

    /**
     * @param hsl
     * @return
     */
    fun HSL2RGB(hsl: HSL?): RGB? {
        if (hsl == null) {
            return null
        }
        val H = hsl.getH()
        val S = hsl.getS()
        val L = hsl.getL()
        var R: Float
        var G: Float
        var B: Float
        val var_1: Float
        var var_2: Float
        if (S == 0f) {
            R = L
            G = L
            B = L
        } else {
            var_2 = if (L < 128) {
                L * (256 + S) / 256
            } else {
                L + S - S * L / 256
            }
            if (var_2 > 255) {
                var_2 = Math.round(var_2).toFloat()
            }
            if (var_2 > 254) {
                var_2 = 255f
            }
            var_1 = 2 * L - var_2
            R = RGBFromHue(var_1, var_2, H + 120)
            G = RGBFromHue(var_1, var_2, H)
            B = RGBFromHue(var_1, var_2, H - 120)
        }
        R = if (R < 0) 0F else R
        R = if (R > 255) 255F else R
        G = if (G < 0) 0F else G
        G = if (G > 255) 255F else G
        B = if (B < 0) 0F else B
        B = if (B > 255) 255F else B
        return RGB(Math.round(R), Math.round(G), Math.round(B))
    }

    /**
     * @param a
     * @param b
     * @param h
     * @return
     */
    fun RGBFromHue(a: Float, b: Float, h: Float): Float {
        var h = h
        if (h < 0) {
            h += 360f
        }
        if (h >= 360) {
            h -= 360f
        }
        if (h < 60) {
            return a + (b - a) * h / 60
        }
        if (h < 180) {
            return b
        }
        return if (h < 240) {
            a + (b - a) * (240 - h) / 60
        } else a
    }
}