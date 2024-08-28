package com.github.anastaciocintra.escpos;

import com.github.anastaciocintra.escpos.barcode.BarCodeWrapperInterface;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;

/* loaded from: classes.dex */
public class EscPos implements Closeable, Flushable, EscPosConst {
    private String charsetName;
    private OutputStream outputStream;
    private Style style;

    /* loaded from: classes.dex */
    public enum CharacterCodeTable {
        CP437_USA_Standard_Europe(0, "cp437"),
        Katakana(1),
        CP850_Multilingual(2, "cp850"),
        CP860_Portuguese(3, "cp860"),
        CP863_Canadian_French(4, "cp863"),
        CP865_Nordic(5, "cp865"),
        CP851_Greek(11),
        CP853_Turkish(12),
        CP857_Turkish(13, "cp857"),
        CP737_Greek(14, "cp737"),
        ISO8859_7_Greek(15, "iso8859_7"),
        WPC1252(16),
        CP866_Cyrillic_2(17, "cp866"),
        CP852_Latin2(18, "cp852"),
        CP858_Euro(19, "cp858"),
        KU42_Thai(20),
        TIS11_Thai(21),
        TIS18_Thai(26),
        TCVN_3_1_Vietnamese(30),
        TCVN_3_2_Vietnamese(31),
        PC720_Arabic(32),
        WPC775_BalticRim(33),
        CP855_Cyrillic(34, "cp855"),
        CP861_Icelandic(35, "cp861"),
        CP862_Hebrew(36, "cp862"),
        CP864_Arabic(37, "cp864"),
        CP869_Greek(38, "cp869"),
        ISO8859_2_Latin2(39, "iso8859_2"),
        ISO8859_15_Latin9(40, "iso8859_15"),
        CP1098_Farsi(41, "cp1098"),
        CP1118_Lithuanian(42),
        CP1119_Lithuanian(43),
        CP1125_Ukrainian(44),
        WCP1250_Latin2(45, "cp1250"),
        WCP1251_Cyrillic(46, "cp1251"),
        WCP1253_Greek(47, "cp1253"),
        WCP1254_Turkish(48, "cp1254"),
        WCP1255_Hebrew(49, "cp1255"),
        WCP1256_Arabic(50, "cp1256"),
        WCP1257_BalticRim(51, "cp1257"),
        WCP1258_Vietnamese(52, "cp1258"),
        KZ_1048_Kazakhstan(53),
        User_defined_page(255);

        public String charsetName;
        public int value;

        CharacterCodeTable(int value) {
            this.value = value;
            this.charsetName = "cp437";
        }

        CharacterCodeTable(int value, String charsetName) {
            this.value = value;
            this.charsetName = charsetName;
        }
    }

    /* loaded from: classes.dex */
    public enum CutMode {
        FULL(48),
        PART(49);

        public int value;

        CutMode(int value) {
            this.value = value;
        }
    }

    /* loaded from: classes.dex */
    public enum PinConnector {
        Pin_2(48),
        Pin_5(49);

        public int value;

        PinConnector(int value) {
            this.value = value;
        }
    }

    public EscPos(OutputStream outputStream) {
        this.outputStream = outputStream;
        setCharsetName(CharacterCodeTable.CP437_USA_Standard_Europe.charsetName);
        this.style = new Style();
    }

    public EscPos write(int b) throws IOException {
        this.outputStream.write(b);
        return this;
    }

    public EscPos write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        return this;
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
        this.outputStream.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.outputStream.close();
    }

    public EscPos setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public EscPos setStyle(Style style) {
        this.style = style;
        return this;
    }

    public Style getStyle() {
        return this.style;
    }

    public final EscPos setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    public String getDefaultCharsetName() {
        return this.charsetName;
    }

    public EscPos setCharacterCodeTable(CharacterCodeTable table) throws IOException, IllegalArgumentException {
        setCharsetName(table.charsetName);
        setPrinterCharacterTable(table.value);
        return this;
    }

    public EscPos setPrinterCharacterTable(int characterCodeTable) throws IOException, IllegalArgumentException {
        if (characterCodeTable < 0 || characterCodeTable > 255) {
            throw new IllegalArgumentException("characterCodeTable must be between 0 and 255");
        }
        write(27);
        write(116);
        write(characterCodeTable);
        return this;
    }

    public EscPos write(Style style, String text) throws UnsupportedEncodingException, IOException {
        byte[] configBytes = style.getConfigBytes();
        write(configBytes, 0, configBytes.length);
        this.outputStream.write(text.getBytes(this.charsetName));
        return this;
    }

    public EscPos write(String text) throws UnsupportedEncodingException, IOException {
        return write(this.style, text);
    }

    public EscPos writeLF(Style style, String text) throws UnsupportedEncodingException, IOException {
        write(style, text);
        write(10);
        return this;
    }

    public EscPos writeLF(String text) throws UnsupportedEncodingException, IOException {
        return writeLF(this.style, text);
    }

    public EscPos write(BarCodeWrapperInterface barcode, String data) throws IOException {
        byte[] bytes = barcode.getBytes(data);
        write(bytes, 0, bytes.length);
        return this;
    }

    public EscPos cut(CutMode mode) throws IOException {
        write(29);
        write(86);
        write(mode.value);
        return this;
    }

    public EscPos feed(Style style, int nLines) throws IOException, IllegalArgumentException {
        if (nLines < 1 || nLines > 255) {
            throw new IllegalArgumentException("nLines must be between 1 and 255");
        }
        if (nLines == 1) {
            write(10);
        } else {
            byte[] configBytes = style.getConfigBytes();
            write(configBytes, 0, configBytes.length);
            write(27);
            write(100);
            write(nLines);
        }
        return this;
    }

    public EscPos feed(int nLines) throws IOException, IllegalArgumentException {
        return feed(this.style, nLines);
    }

    public EscPos initializePrinter() throws IOException {
        write(27);
        write(64);
        this.style.reset();
        return this;
    }

    public EscPos pulsePin(PinConnector pinConnector, int t1, int t2) throws IOException, IllegalArgumentException {
        if (t1 < 0 || t1 > 255) {
            throw new IllegalArgumentException("t1 must be between 1 and 255");
        }
        if (t2 < 0 || t2 > 255) {
            throw new IllegalArgumentException("t2 must be between 1 and 255");
        }
        write(27);
        write(112);
        write(pinConnector.value);
        write(t1);
        write(t2);
        return this;
    }

    public EscPos info() throws UnsupportedEncodingException, IOException {
        Properties properties = new Properties();
        properties.load((InputStream) Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("projectinfo.properties")));
        String Version = properties.getProperty("version");
        Style title = new Style().setFontSize(Style.FontSize._3, Style.FontSize._3).setColorMode(Style.ColorMode.WhiteOnBlack).setJustification(Justification.Center);
        write(title, "EscPos Coffee");
        feed(5);
        writeLF("java driver for ESC/POS commands.");
        writeLF("Version: " + Version);
        feed(3);
        getStyle().setJustification(Justification.Right);
        writeLF("github.com");
        writeLF("anastaciocintra/escpos-coffee");
        feed(5);
        cut(CutMode.FULL);
        return this;
    }
}
