package com.dreamspace.superman.Common.QRCode;

import android.graphics.Bitmap;

import com.google.zxing.common.BitMatrix;



public class MatrixToImageWriter {

    private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

    private MatrixToImageWriter() {
    }

    /**
     * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
     * as white, and "true" bits are rendered as black. Uses default
     * configuration.
     *
     * @param matrix {@link BitMatrix} to write
     * @return {@link Bitmap} representation of the input
     */
    public static Bitmap toBitmap(BitMatrix matrix) {
        return toBitmap(matrix, DEFAULT_CONFIG);
    }

    /**
     * As {@link #toBitmap(BitMatrix)}, but allows customization of the output.
     *
     * @param matrix {@link BitMatrix} to write
     * @param config output configuration
     * @return {@link Bitmap} representation of the input
     */
    public static Bitmap toBitmap(BitMatrix matrix, MatrixToImageConfig config) {
        final int onColor = config.getPixelOnColor();
        final int offColor = config.getPixelOffColor();
        final int width = matrix.getWidth();
        final int height = matrix.getHeight();
        final int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? onColor : offColor;
            }
        }

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.setPixels(pixels, 0, width, 0, 0, width, height);
        return image;
    }







}
