package palette

import java.awt.Color

class RGB {
    private var red = 0
    private var green = 0
    private var blue = 0

    constructor()
    constructor(red: Int, green: Int, blue: Int) {
        setRed(red)
        setBlue(blue)
        setGreen(green)
    }

    fun getRed(): Int {
        return red
    }

    fun setRed(red: Int) {
        if (red < 0) {
            this.red = 0
        } else if (red > 255) {
            this.red = 255
        } else {
            this.red = red
        }
    }

    fun getGreen(): Int {
        return green
    }

    fun setGreen(green: Int) {
        if (green < 0) {
            this.green = 0
        } else if (green > 255) {
            this.green = 255
        } else {
            this.green = green
        }
    }

    fun getBlue(): Int {
        return blue
    }

    fun setBlue(blue: Int) {
        if (blue < 0) {
            this.blue = 0
        } else if (blue > 255) {
            this.blue = 255
        } else {
            this.blue = blue
        }
    }

    override fun equals(obj: Any?): Boolean {
        val theRGB = obj as RGB?
        return getRed() == theRGB!!.getRed() && getGreen() == theRGB.getGreen() && getBlue() == theRGB.getBlue()
    }

    fun toJavaColor(): Color {
        return Color(red, green, blue)
    }

    override fun hashCode(): Int {
        return getRed() * 1000000 + getGreen() * 1000 + getBlue()
    }

    override fun toString(): String {
        return "RGB {" + red + ", " + green + ", " + blue + "}"
    }
}