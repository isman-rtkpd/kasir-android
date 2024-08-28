package com.github.anastaciocintra.escpos;

/* loaded from: classes.dex */
public interface EscPosConst {
    public static final int ESC = 27;
    public static final int GS = 29;
    public static final int LF = 10;
    public static final int NUL = 0;

    /* loaded from: classes.dex */
    public enum Justification {
        Left_Default(48),
        Center(49),
        Right(50);

        public int value;

        Justification(int value) {
            this.value = value;
        }
    }
}
