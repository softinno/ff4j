package org.ff4j.utils;

/*-
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 - 2019 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Allow to generate color Gradients.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class ColorsUtils {
    
    /** Hide default constructor. */
    private ColorsUtils() {}

    /** Start Color. */
    private static final String START_COLOR = "00AB8B";
    /** End Color. */
    private static final String END_COLOR = "EEFFEE";
    
    /** HSV Colors. */
    public static final String START_RAINBOW = "ee1100";
    public static final String END_RAINBOW   = "442299";
    
    /**
     * This code build the color gradient between 2 colors with defined step.
     * @param codeFrom
     *      color source
     * @param codeTo
     *      color destination
     * @param nbDivision
     *      number of steps
     * @return
     *      the list of colors
     */
    public static List < String > getRGBGradientColors(String codeFrom, String codeTo, int nbDivision) {
        List < String > colors = new ArrayList<String>();
        if (nbDivision < 1) {
           nbDivision = 1;
        }
        nbDivision++;
        int r1 = Integer.parseInt(codeFrom.substring(0, 2), 16);
        int g1 = Integer.parseInt(codeFrom.substring(2, 4), 16);
        int b1 = Integer.parseInt(codeFrom.substring(4, 6), 16);
        int r2 = Integer.parseInt(codeTo.substring(0, 2), 16);
        int g2 = Integer.parseInt(codeTo.substring(2, 4), 16);
        int b2 = Integer.parseInt(codeTo.substring(4, 6), 16);
        int rDelta = (r2 - r1) / nbDivision;
        int gDelta = (g2 - g1) / nbDivision;
        int bDelta = (b2 - b1) / nbDivision;
        for (int idx = 0;idx < nbDivision;idx++) {
            String red   = Integer.toHexString(r1 + rDelta * idx);
            String green = Integer.toHexString(g1 + gDelta * idx);
            String blue  = Integer.toHexString(b1 + bDelta * idx);
            colors.add(red + green + blue);
        }
        return colors.subList(1, colors.size());
    }
    
    public static List < String > getHSVGradientColors(String codeFrom, String codeTo, int nbDivision) {
        int r1 = Integer.parseInt(codeFrom.substring(0, 2), 16);
        int g1 = Integer.parseInt(codeFrom.substring(2, 4), 16);
        int b1 = Integer.parseInt(codeFrom.substring(4, 6), 16);
        int r2 = Integer.parseInt(codeTo.substring(0, 2), 16);
        int g2 = Integer.parseInt(codeTo.substring(2, 4), 16);
        int b2 = Integer.parseInt(codeTo.substring(4, 6), 16);
        float[] startHSB = Color.RGBtoHSB(r1, g1, b1, null);
        float[] endHSB   = Color.RGBtoHSB(r2, g2, b2, null);
        float brightness = (startHSB[2] + endHSB[2]) / 2;
        float saturation = (startHSB[1] + endHSB[1]) / 2;
        float hueMax = 0;
        float hueMin = 0;
        if (startHSB[0] > endHSB[0]) {
            hueMax = startHSB[0];
            hueMin = endHSB[0];
        } else {
            hueMin = startHSB[0];
            hueMax = endHSB[0];
        }
        List < String > colors = new ArrayList<String>();
        for (int idx = 0;idx < nbDivision;idx++) {
            float hue = ((hueMax - hueMin) * idx/nbDivision) + hueMin;
            int rgbColor = Color.HSBtoRGB(hue, saturation, brightness);
            colors.add(Integer.toHexString(rgbColor).substring(2));
        }
        return colors;
    }
    
    
    /**
     * Dedicated gradient for ff4j console (Pie Chart).
     *
     * @param nbsectors
     *      target sectors
     * @return
     *      color gradient
     */
    public static List < String > getColorsGradient(int nbsectors) {
        return getRGBGradientColors(START_COLOR, END_COLOR, nbsectors);
    }
    
    /**
     * Dedicated gradient for ff4j console (Pie Chart).
     *
     * @param nbsectors
     *      target sectors
     * @return
     *      color gradient
     */
    public static List < String > getColorsRainbowt(int nbsectors) {
        return getHSVGradientColors(START_RAINBOW, END_RAINBOW, nbsectors);
    }

}
