package palette

import java.awt.image.BufferedImage

/**
 * This program demonstrates how to resize an image.
 *
 * @author www.codejava.net
 */
object ImageResizer {
    fun resize(source: BufferedImage, width: Int, height: Int): BufferedImage{
        val result = BufferedImage(width, height, source.type)
        val g2d = result.createGraphics()
        g2d.drawImage(source, 0, 0, width, height, null)
        g2d.dispose()
        return result
    }
}