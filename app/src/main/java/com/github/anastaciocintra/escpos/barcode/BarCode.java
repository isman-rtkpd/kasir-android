package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class BarCode implements EscPosConst, BarCodeWrapperInterface {
    private BarCodeSystem sytem = BarCodeSystem.CODE93_Default;
    private int width = 2;
    private int height = 100;
    private BarCodeHRIPosition HRIPosition = BarCodeHRIPosition.NotPrinted_Default;
    private BarCodeHRIFont HRIFont = BarCodeHRIFont.Font_A_Default;
    private Justification justification = Justification.Left_Default;

    /* loaded from: classes.dex */
    public enum BarCodeSystem {
        UPCA(0, "\\d{11,12}$"),
        UPCA_B(65, "^\\d{11,12}$"),
        UPCE_A(1, "^\\d{6}$|^0{1}\\d{6,7}$|^0{1}\\d{10,11}$"),
        UPCE_B(66, "^\\d{6}$|^0{1}\\d{6,7}$|^0{1}\\d{10,11}$"),
        JAN13_A(2, "^\\d{12,13}$"),
        JAN13_B(67, "^\\d{12,13}$"),
        JAN8_A(3, "^\\d{7,8}$"),
        JAN8_B(68, "^\\d{7,8}$"),
        CODE39_A(4, "^[\\d\\p{Upper}\\ \\$\\%\\*\\+\\-\\.\\/]+$"),
        CODE39_B(69, "^[\\d\\p{Upper}\\ \\$\\%\\*\\+\\-\\.\\/]+$"),
        ITF_A(5, "^([\\d]{2})+$"),
        ITF_B(70, "^([\\d]{2})+$"),
        CODABAR_A(6, "^[A-Da-d][\\d\\$\\+\\-\\.\\/\\:]*[A-Da-d]$"),
        CODABAR_B(71, "^[A-Da-d][\\d\\$\\+\\-\\.\\/\\:]*[A-Da-d]$"),
        CODE93_Default(72, "^[\\x00-\\x7F]+$"),
        CODE128(73, "^\\{[A-C][\\x00-\\x7F]+$");

        public int code;
        public String regex;

        BarCodeSystem(int code, String regex) {
            this.code = code;
            this.regex = regex;
        }
    }

    /* loaded from: classes.dex */
    public enum BarCodeHRIPosition {
        NotPrinted_Default(48),
        AboveBarCode(49),
        BelowBarCode(50),
        AboveAndBelowBarCode(51);

        public int value;

        BarCodeHRIPosition(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum BarCodeHRIFont {
        Font_A_Default(48),
        Font_B(49),
        Font_C(50);

        public int value;

        BarCodeHRIFont(int value) {
            this.value = value;
        }
    }

    public BarCode setSystem(BarCodeSystem barCodeSystem) {
        this.sytem = barCodeSystem;
        return this;
    }

    public BarCode setBarCodeSize(int width, int height) throws IllegalArgumentException {
        if ((width < 2 || width > 6) && (width < 68 || width > 76)) {
            throw new IllegalArgumentException("with must be between 1 and 255");
        }
        if (height < 1 || height > 255) {
            throw new IllegalArgumentException("height must be between 1 and 255");
        }
        this.width = width;
        this.height = height;
        return this;
    }

    public BarCode setHRIPosition(BarCodeHRIPosition barCodeHRI) {
        this.HRIPosition = barCodeHRI;
        return this;
    }

    public BarCode setHRIFont(BarCodeHRIFont HRIFont) {
        this.HRIFont = HRIFont;
        return this;
    }

    public BarCode setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    @Override // com.github.anastaciocintra.escpos.barcode.BarCodeWrapperInterface
    public byte[] getBytes(String data) throws IllegalArgumentException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (!data.matches(this.sytem.regex)) {
            throw new IllegalArgumentException(String.format("data must match with \"%s\"", this.sytem.regex));
        }
        bytes.write(29);
        bytes.write(104);
        bytes.write(this.height);
        bytes.write(29);
        bytes.write(119);
        bytes.write(this.width);
        bytes.write(29);
        bytes.write(72);
        bytes.write(this.HRIPosition.value);
        bytes.write(29);
        bytes.write(102);
        bytes.write(this.HRIFont.value);
        bytes.write(27);
        bytes.write(97);
        bytes.write(this.justification.value);
        bytes.write(29);
        bytes.write(107);
        bytes.write(this.sytem.code);
        if (this.sytem.code <= 6) {
            bytes.write(data.getBytes(), 0, data.length());
            bytes.write(0);
        } else {
            bytes.write(data.length());
            bytes.write(data.getBytes(), 0, data.length());
        }
        return bytes.toByteArray();
    }
}
