package com.github.anastaciocintra.escpos.barcode;

import com.github.anastaciocintra.escpos.EscPosConst;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class QRCode implements EscPosConst, BarCodeWrapperInterface {
    private Justification justification = Justification.Left_Default;
    private QRModel model = QRModel._1_Default;
    private int size = 3;
    private QRErrorCorrectionLevel errorCorrectionLevel = QRErrorCorrectionLevel.QR_ECLEVEL_M_Default;

    /* loaded from: classes.dex */
    public enum QRModel {
        _1_Default(48),
        _2(49);

        public int value;

        QRModel(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum QRErrorCorrectionLevel {
        QR_ECLEVEL_L(48),
        QR_ECLEVEL_M_Default(49),
        QR_ECLEVEL_Q(50),
        QR_ECLEVEL_H(51);

        public int value;

        QRErrorCorrectionLevel(int value) {
            this.value = value;
        }
    }

    public QRCode setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    public QRCode setModel(QRModel model) {
        this.model = model;
        return this;
    }

    public QRCode setSize(int size) throws IllegalArgumentException {
        if (size < 1 || size > 16) {
            throw new IllegalArgumentException("size must be between 1 and 16");
        }
        this.size = size;
        return this;
    }

    public QRCode setErrorCorrectionLevel(QRErrorCorrectionLevel errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
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
        bytes.write(4);
        bytes.write(0);
        bytes.write(49);
        bytes.write(65);
        bytes.write(this.model.value);
        bytes.write(0);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(49);
        bytes.write(67);
        bytes.write(this.size);
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(49);
        bytes.write(69);
        bytes.write(this.errorCorrectionLevel.value);
        int numberOfBytes = data.length() + 3;
        int pL = numberOfBytes & 255;
        int pH = (65280 & numberOfBytes) >> 8;
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(pL);
        bytes.write(pH);
        bytes.write(49);
        bytes.write(80);
        bytes.write(48);
        bytes.write(data.getBytes(), 0, data.length());
        bytes.write(29);
        bytes.write(40);
        bytes.write(107);
        bytes.write(3);
        bytes.write(0);
        bytes.write(49);
        bytes.write(81);
        bytes.write(48);
        return bytes.toByteArray();
    }
}
