/*
 * Created by Davide Cossu (gjkf), 11/13/2016
 */

package io.github.gjkf.seriousEngine.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * A particular texture used for text rendering.
 */

public class FontTexture{

    /**
     * The extension of the image.
     */
    private static final String IMAGE_FORMAT = "png";
    /**
     * The font.
     */
    private final Font font;
    /**
     * The charset.
     */
    private final String charSetName;
    /**
     * A charmap that will contain all characters.
     */
    private final Map<Character, CharInfo> charMap;
    /**
     * The texture
     */
    private Texture texture;
    /**
     * The width.
     */
    private int height;
    /**
     * The height.
     */
    private int width;

    /**
     * Constructs a new texture
     *
     * @param font        The {@link Font} object.
     * @param charSetName The charset name.
     *
     * @throws Exception If anything went wrong.
     */

    public FontTexture(Font font, String charSetName) throws Exception{
        this.font = font;
        this.charSetName = charSetName;
        charMap = new HashMap<>();

        buildTexture();
    }

    /**
     * Getter for property 'width'.
     *
     * @return Value for property 'width'.
     */

    public int getWidth(){
        return width;
    }

    /**
     * Getter for property 'height'.
     *
     * @return Value for property 'height'.
     */

    public int getHeight(){
        return height;
    }

    /**
     * Getter for property 'texture'.
     *
     * @return Value for property 'texture'.
     */

    public Texture getTexture(){
        return texture;
    }

    /**
     * Gets the {@link CharInfo} object corresponding to the given char.
     *
     * @param c The char.
     *
     * @return The info.
     */

    public CharInfo getCharInfo(char c){
        return charMap.get(c);
    }

    /**
     * Gets all the chars in the charset.
     *
     * @param charsetName The charset.
     *
     * @return A string with all the chars.
     */

    private String getAllAvailableChars(String charsetName){
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for(char c = 0; c < Character.MAX_VALUE; c++){
            if(ce.canEncode(c)){
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Builds a temporary texture.
     *
     * @throws Exception If anything went wrong.
     */

    private void buildTexture() throws Exception{
        // Get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charSetName);
        this.width = 0;
        this.height = 0;
        for(char c : allChars.toCharArray()){
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            width += charInfo.getWidth();
            height = Math.max(height, fontMetrics.getHeight());
        }
        g2D.dispose();

        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();

        // Dump image to a byte buffer
        InputStream is;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(img, IMAGE_FORMAT, out);
            out.flush();
            is = new ByteArrayInputStream(out.toByteArray());
        }

        texture = new Texture(is);
    }

    public static class CharInfo{

        private final int startX;

        private final int width;

        public CharInfo(int startX, int width){
            this.startX = startX;
            this.width = width;
        }

        /**
         * Getter for property 'startX'.
         *
         * @return Value for property 'startX'.
         */
        public int getStartX(){
            return startX;
        }

        /**
         * Getter for property 'width'.
         *
         * @return Value for property 'width'.
         */
        public int getWidth(){
            return width;
        }
    }

}