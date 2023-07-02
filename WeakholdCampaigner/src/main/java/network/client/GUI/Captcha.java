package network.client.GUI;

import javafx.scene.image.Image;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.util.Random;

import static network.client.GUI.AbstractMenu.PIXEL_UNIT;

public class Captcha {
    public static Image getCaptcha(String randomCaptchaNumber) {
        return bufferedImageToImage(
                getCaptchaAsBufferedImage(randomCaptchaNumber, PIXEL_UNIT * 5, PIXEL_UNIT * 5));
    }

    private static int randomNum(int num) {
        return (new Random()).nextInt(num);
    }

    private static Color randomColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + randomNum(bc - fc);
        int g = fc + randomNum(bc - fc);
        int b = fc + randomNum(bc - fc);
        return new Color(r, g, b);
    }

    private static BufferedImage getCaptchaAsBufferedImage(String captchaKey, int width, int height) {
        Font font = new Font("Verdana", Font.ITALIC
                | Font.BOLD, 28);

        char[] chars = captchaKey.toCharArray();

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D bufferedImageGraphics = (Graphics2D) bufferedImage.getGraphics();
        AlphaComposite ac3;
        Color color;
        int len = chars.length;
        bufferedImageGraphics.setColor(Color.WHITE);
        bufferedImageGraphics.fillRect(0, 0, width, height);

        //key generation:
        bufferedImageGraphics.setFont(font);
        int h = height - ((height - font.getSize()) >> 1), w = width
                / len, size = w - font.getSize() + 1;
        for (int i = 0; i < len; i++) {
            ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    0.7f);
            bufferedImageGraphics.setComposite(ac3);

            color = new Color(20 + randomNum(110), 30 + randomNum(110),
                    30 + randomNum(110));
            bufferedImageGraphics.setColor(color);
            bufferedImageGraphics.drawString(chars[i] + "", (width - (len - i) * w) + size,
                    h - 4);
        }

        //Noise generation:
        for (int i = 0; i < (width * height / 100); i++) {
            color = randomColor(150, 250);
            bufferedImageGraphics.setColor(color);
            bufferedImageGraphics.drawOval(randomNum(width), randomNum(height), 5 + randomNum(width/30),
                    5 + randomNum(height/30));
        }
        for (int i = 0; i < (width * height / 700); i++) {
            color = randomColor(150, 250);
            bufferedImageGraphics.setColor(color);

            bufferedImageGraphics.drawLine(randomNum(width), randomNum(height), randomNum(width), randomNum(height));
        }


        return bufferedImage;
    }

    private static Image bufferedImageToImage(BufferedImage img){
        //javafx.scene.image.Image

        //converting to a good type, read about types here:
        // https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        newImg.createGraphics().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);

        //converting the BufferedImage to an IntBuffer
        int[] type_int_agrb = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
        IntBuffer buffer = IntBuffer.wrap(type_int_agrb);

        //converting the IntBuffer to an Image, read more about it here:
        // https://openjfx.io/javadoc/13/javafx.graphics/javafx/scene/image/PixelBuffer.html
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(newImg.getWidth(), newImg.getHeight(),
                buffer, pixelFormat);
        return new WritableImage(pixelBuffer);
    }
}
