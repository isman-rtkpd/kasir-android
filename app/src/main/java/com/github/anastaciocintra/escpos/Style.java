package com.github.anastaciocintra.escpos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class Style implements EscPosConst {
    private boolean bold;
    private ColorMode colorMode;
    private boolean defaultLineSpacing;
    private FontSize fontHeight;
    private FontName fontName;
    private FontSize fontWidth;
    private Justification justification;
    private int lineSpacing;
    private Underline underline;

    /* loaded from: classes.dex */
    public enum FontName {
        Font_A_Default(48),
        Font_B(49),
        Font_C(50);

        public int value;

        FontName(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum FontSize {
        _1(0),
        _2(1),
        _3(2),
        _4(3),
        _5(4),
        _6(5),
        _7(6),
        _8(7);

        public int value;

        FontSize(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum Underline {
        None_Default(48),
        OneDotThick(49),
        TwoDotThick(50);

        public int value;

        Underline(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum ColorMode {
        BlackOnWhite_Default(0),
        WhiteOnBlack(1);

        public int value;

        ColorMode(int value) {
            this.value = value;
        }
    }

    public Style() {
        reset();
    }

    public Style(Style another) {
        setFontName(another.fontName);
        setBold(another.bold);
        setFontSize(another.fontWidth, another.fontHeight);
        setUnderline(another.underline);
        setJustification(another.justification);
        this.defaultLineSpacing = another.defaultLineSpacing;
        setLineSpacing(another.lineSpacing);
        setColorMode(another.colorMode);
    }

    public final void reset() {
        this.fontName = FontName.Font_A_Default;
        this.fontWidth = FontSize._1;
        this.fontHeight = FontSize._1;
        this.bold = false;
        this.underline = Underline.None_Default;
        this.justification = Justification.Left_Default;
        resetLineSpacing();
        this.colorMode = ColorMode.BlackOnWhite_Default;
    }

    public final Style setFontName(FontName fontName) {
        this.fontName = fontName;
        return this;
    }

    public final Style setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public final Style setFontSize(FontSize fontWidth, FontSize fontHeight) {
        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;
        return this;
    }

    public final Style setUnderline(Underline underline) {
        this.underline = underline;
        return this;
    }

    public final Style setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    public final Style setLineSpacing(int lineSpacing) throws IllegalArgumentException {
        if (lineSpacing < 0 || lineSpacing > 255) {
            throw new IllegalArgumentException("lineSpacing must be between 0 and 255");
        }
        this.defaultLineSpacing = false;
        this.lineSpacing = lineSpacing;
        return this;
    }

    public final Style resetLineSpacing() {
        this.defaultLineSpacing = true;
        this.lineSpacing = 0;
        return this;
    }

    public final Style setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        return this;
    }

    public byte[] getConfigBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(27);
        byteArrayOutputStream.write(77);
        byteArrayOutputStream.write(this.fontName.value);
        byteArrayOutputStream.write(27);
        byteArrayOutputStream.write(69);
        byteArrayOutputStream.write(this.bold ? 1 : 0);
        int i = (this.fontWidth.value << 4) | this.fontHeight.value;
        byteArrayOutputStream.write(29);
        byteArrayOutputStream.write(33);
        byteArrayOutputStream.write(i);
        byteArrayOutputStream.write(27);
        byteArrayOutputStream.write(45);
        byteArrayOutputStream.write(this.underline.value);
        byteArrayOutputStream.write(27);
        byteArrayOutputStream.write(97);
        byteArrayOutputStream.write(this.justification.value);
        if (this.defaultLineSpacing) {
            byteArrayOutputStream.write(27);
            byteArrayOutputStream.write(50);
        } else {
            byteArrayOutputStream.write(27);
            byteArrayOutputStream.write(51);
            byteArrayOutputStream.write(this.lineSpacing);
        }
        byteArrayOutputStream.write(29);
        byteArrayOutputStream.write(66);
        byteArrayOutputStream.write(this.colorMode.value);
        return byteArrayOutputStream.toByteArray();
    }
}
