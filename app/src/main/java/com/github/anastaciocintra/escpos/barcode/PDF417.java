package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class PDF417 implements EscPosConst, BarCodeWrapperInterface {
    private Justification justification = Justification.Left_Default;
    private int numberOfColumns = 0;
    private int numberOfRows = 0;
    private int width = 3;
    private int height = 3;
    private PDF417ErrorLevel errorLevel = PDF417ErrorLevel._1_Default;
    private PDF417Option option = PDF417Option.Standard_Default;

    /* loaded from: classes.dex */
    public enum PDF417ErrorLevel {
        _0(48),
        _1_Default(49),
        _2(50),
        _3(51),
        _4(52),
        _5(53),
        _6(54),
        _7(55),
        _8(56);

        public int value;

        PDF417ErrorLevel(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum PDF417Option {
        Standard_Default(0),
        Truncated(1);

        public int value;

        PDF417Option(int value) {
            this.value = value;
        }
    }

    public PDF417 setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    public PDF417 setNumberOfColumns(int numberOfColumns) throws IllegalArgumentException {
        if (numberOfColumns < 0 || numberOfColumns > 30) {
            throw new IllegalArgumentException("numberOfColumns must be between 0 and 30");
        }
        this.numberOfColumns = numberOfColumns;
        return this;
    }

    public PDF417 setNumberOfRows(int numberOfRows) throws IllegalArgumentException {
        int i;
        if (numberOfRows != 0 && ((i = this.numberOfColumns) < 3 || i > 90)) {
            throw new IllegalArgumentException("numberOfRows must be 0 or between 3 and 90");
        }
        this.numberOfRows = numberOfRows;
        return this;
    }

    public PDF417 setWidth(int width) throws IllegalArgumentException {
        if (width < 2 || width > 8) {
            throw new IllegalArgumentException("width must be between 2 and 8");
        }
        this.width = width;
        return this;
    }

    public PDF417 setHeight(int height) throws IllegalArgumentException {
        if (height < 2 || height > 8) {
            throw new IllegalArgumentException("height must be between 2 and 8");
        }
        this.height = height;
        return this;
    }

    public PDF417 setErrorLevel(PDF417ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
        return this;
    }

    public PDF417 setOption(PDF417Option option) {
        this.option = option;
        return this;
    }

    @Override // com.github.anastaciocintra.escpos.barcode.BarCodeWrapperInterface
    public byte[] getBytes(String data) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(27);
        bytes.write(97);
        bytes.write(this.justification.value);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(65);
        bytes.write(this.numberOfColumns);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(66);
        bytes.write(this.numberOfRows);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(67);
        bytes.write(this.width);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(68);
        bytes.write(this.height);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(4);
        bytes.write(0);
        bytes.write(48);
        bytes.write(69);
        bytes.write(48);
        bytes.write(this.errorLevel.value);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(70);
        bytes.write(this.option.value);
        int numberOfBytes = data.length() + 3;
        int pL = numberOfBytes & 255;
        int pH = (65280 & numberOfBytes) >> 8;
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(pL);
        bytes.write(pH);
        bytes.write(48);
        bytes.write(80);
        bytes.write(48);
        bytes.write(data.getBytes(), 0, data.length());
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(48);
        bytes.write(81);
        bytes.write(48);
        return bytes.toByteArray();
    }
}
