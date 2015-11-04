package com.dreamspace.superman.Common.QRCode;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCode extends AbstractQRCode {

    protected final String text;

    protected QRCode(String text) {
        this.text = text;
        qrWriter = new QRCodeWriter();
    }

    /**
     * Create a QR code from the given text. <br/>
     * <br/>
     * <p/>
     * There is a size limitation to how much you can put into a QR code. This
     * has been tested to work with up to a length of 2950 characters.<br/>
     * <br/>
     * <p/>
     * The QRCode will have the following defaults: <br/>
     * {size: 100x100}<br/>
     * {imageType:PNG} <br/>
     * <br/>
     * <p/>
     * Both size and imageType can be overridden: <br/>
     * Image type override is done by calling
     * {@link AbstractQRCode#to(com.dreamspace.superman.Common.QRCode.ImageType)} e.g.
     * QRCode.from("hello world").to(JPG) <br/>
     * Size override is done by calling {@link AbstractQRCode#withSize} e.g.
     * QRCode.from("hello world").to(JPG).withSize(125, 125) <br/>
     *
     * @param text the text to encode to a new QRCode, this may fail if the text
     *             is too large. <br/>
     * @return the QRCode object <br/>
     */
    public static QRCode from(String text) {
        return new QRCode(text);
    }


    /**
     * Returns a {@link android.graphics.Bitmap} without creating a {@link java.io.File} first.
     *
     * @return {@link android.graphics.Bitmap} of this QRCode
     */
    public Bitmap bitmap() {
        try {
            return MatrixToImageWriter.toBitmap(createMatrix(text));
        } catch (WriterException e) {
            throw new RuntimeException("Failed to create QR image from text due to underlying exception", e);
        }
    }

}
