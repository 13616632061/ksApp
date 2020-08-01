package com.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/3/10.
 */

public class MakeXml {
    private final static String rootPath = "F:\\layoutroot\\values-{0}x{1}\\";

    private final static double dw = 320;
    private final static double dh = 480;

    private final static String WTemplate = "<dimen name=\"layout_x_{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "<dimen name=\"layout_y_{0}\">{1}px</dimen>\n";

    public static void main(String[] args) {
        makeString(320, 480);
        makeString(480, 800);
        makeString(480, 854);
        makeString(540, 960);
        makeString(600, 1024);
        makeString(720, 1184);
        makeString(720, 1196);
        makeString(720, 1280);
        makeString(768, 1024);
        makeString(800, 1280);
        makeString(1080, 1812);
        makeString(1080, 1920);
        makeString(1366, 720);
        makeString(1366, 768);
        makeString(1440, 2560);
    }

    public static void makeString(int w, int h) {

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<resources>");
        double cellw = (w / dw)*(0.67);
        for (int i = 0; i < 480; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sb.append(WTemplate.replace("{0}", "480").replace("{1}", w + ""));
        sb.append("</resources>");

        StringBuffer sb2 = new StringBuffer();
        sb2.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb2.append("<resources>");
        double cellh = (h / dh)*(0.67);
        for (int i = 0; i < 800; i++) {
            sb2.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sb2.append(HTemplate.replace("{0}", "800").replace("{1}", h + ""));
        sb2.append("</resources>");

        String path = rootPath.replace("{0}", h + "").replace("{1}", w + "");
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File layxFile = new File(path + "lay_x.xml");
        File layyFile = new File(path + "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sb.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sb2.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static double change(double a) {
        int temp = (int) (a * 100);
        return temp / 100;
    }

}