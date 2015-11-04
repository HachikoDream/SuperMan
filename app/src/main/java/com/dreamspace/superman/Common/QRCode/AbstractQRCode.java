package com.dreamspace.superman.Common.QRCode;


import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


import java.util.HashMap;

/**
 * QRCode generator. This is a simple class that is built on top of <a href="http://code.google.com/p/zxing/">ZXING</a><br/><br/>
 * <p/>
 * Please take a look at their framework, as it has a lot of features. <br/> This small project is just a wrapper that gives a
 * convenient interface to work with. <br/><br/>
 * <p/>
 */
public abstract class AbstractQRCode {

    protected final HashMap<EncodeHintType, Object> hints = new HashMap<>();

    protected Writer qrWriter;

    protected int width = 125;

    protected int height = 125;

    protected ImageType imageType = ImageType.PNG;

    /**
     * Overrides the imageType from its default {@link ImageType#PNG}
     *
     * @param imageType the {@link ImageType} you would like the resulting QR to be
     * @return the current QRCode object
     */
    public AbstractQRCode to(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

    /**
     * Overrides the size of the qr from its default 125x125
     *
     * @param width  the width in pixels
     * @param height the height in pixels
     * @return the current QRCode object
     */
    public AbstractQRCode withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Overrides the default charset by supplying a {@link com.google.zxing.EncodeHintType#CHARACTER_SET} hint to {@link
     * com.google.zxing.qrcode.QRCodeWriter#encode}
     *
     * @return the current QRCode object
     */
    public AbstractQRCode withCharset(String charset) {
        return withHint(EncodeHintType.CHARACTER_SET, charset);
    }

    /**
     * Overrides the default error correction by supplying a {@link com.google.zxing.EncodeHintType#ERROR_CORRECTION} hint to
     * {@link com.google.zxing.qrcode.QRCodeWriter#encode}
     *
     * @return the current QRCode object
     */
    public AbstractQRCode withErrorCorrection(ErrorCorrectionLevel level) {
        return withHint(EncodeHintType.ERROR_CORRECTION, level);
    }

    /**
     * Sets hint to {@link com.google.zxing.qrcode.QRCodeWriter#encode}
     *
     * @return the current QRCode object
     */
    public AbstractQRCode withHint(EncodeHintType hintType, Object value) {
        hints.put(hintType, value);
        return this;
    }
    protected BitMatrix createMatrix(String text) throws WriterException {
        return qrWriter.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);
    }
}
