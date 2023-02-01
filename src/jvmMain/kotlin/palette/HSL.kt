package palette

class HSL {
    var h1 = 0f

    /** 饱和度  */
    var s1 = 0f

    /** 深度  */
    var l1 = 0f

    constructor()
    constructor(h: Float, s: Float, l: Float) {
        setH(h)
        setS(s)
        setL(l)
    }

    fun getH(): Float {
        return h1
    }

    fun setH(h: Float) {
        if (h < 0) {
            this.h1 = 0f
        } else if (h > 360) {
            this.h1 = 360f
        } else {
            this.h1 = h
        }
    }

    fun getS(): Float {
        return s1
    }

    fun setS(s: Float) {
        if (s < 0) {
            this.s1 = 0f
        } else if (s > 255) {
            this.s1 = 255f
        } else {
            this.s1 = s
        }
    }

    fun getL(): Float {
        return l1
    }

    fun setL(l: Float) {
        if (l < 0) {
            this.l1 = 0f
        } else if (l > 255) {
            this.l1 = 255f
        } else {
            this.l1 = l
        }
    }

    override fun equals(obj: Any?): Boolean {
        val theHSL = obj as HSL?
        return getH() == theHSL!!.getH() && getS() == theHSL.getS() && getL() == theHSL.getL()
    }

    override fun hashCode(): Int {
        return java.lang.Float.valueOf(getH() * 1000000 + getS() * 1000 + getL()).toInt()
    }

    override fun toString(): String {
        return "HSL {$h1, $s1, $l1}"
    }
}