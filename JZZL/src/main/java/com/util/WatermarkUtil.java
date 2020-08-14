package com.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mrlu
 * @createTime 2020/7/5
 * @describe 制作水印
 */
public class WatermarkUtil {


    /**
     * 设置二维码
     *
     * @param contents 二维码文本
     * @param width    宽
     * @param height   高
     * @return BufferedImage |
     * @author Mrlu
     * @createTime 2020/8/14 14:32
     */
    public static BufferedImage enQRCode(String contents, int width, int height) throws WriterException {
        //定义二维码参数
        final HashMap hints = new HashMap(8) {
            {
                //编码
                put(EncodeHintType.CHARACTER_SET, "UTF-8");
                //容错级别
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                //边距
                put(EncodeHintType.MARGIN, 0);
            }
        };
        return enQRCode(contents, width, height, hints);
    }


    /**
     * 生成二维码
     *
     * @param contents 二维码内容
     * @param width    图片宽度
     * @param height   图片高度
     * @param hints    二维码相关参数
     * @return BufferedImage对象
     * @throws WriterException 编码时出错
     * @throws IOException     写入文件出错
     */
    public static BufferedImage enQRCode(String contents, int width, int height, Map hints) throws WriterException {
//        String uuid = UUID.randomUUID().toString().replace("-", "");
        //本地完整路径
//        String pathname = path + "/" + uuid + "." + format;
        //生成二维码
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
//        Path file = new File(pathname).toPath();
        //将二维码保存到路径下
//        MatrixToImageWriter.writeToPa(bitMatrix, format, file);
//        return pathname;
        return transparentImage(MatrixToImageWriter.toBufferedImage(bitMatrix), 10);
    }

    public static BufferedImage transparentImage(BufferedImage srcImage,
                                                 int alpha) {
        int imgHeight = srcImage.getHeight();//取得图片的长和宽
        int imgWidth = srcImage.getWidth();
        int c = srcImage.getRGB(3, 3);
        //防止越位
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }
        BufferedImage tmpImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        for (int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for (int j = 0; j < imgHeight; ++j) {
                //把背景设为透明
                if (srcImage.getRGB(i, j) == c) {
                    tmpImg.setRGB(i, j, c & 0x00ffffff);
                }
                //设置透明度
                else {
                    int rgb = tmpImg.getRGB(i, j);
                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                    tmpImg.setRGB(i, j, rgb);
                }
            }
        }
        return tmpImg;
    }

    /**
     * 将图片绘制在背景图上
     *
     * @param backgroundPath 背景图路径
     * @param zxingImage     图片
     * @param x              图片在背景图上绘制的x轴起点
     * @param y              图片在背景图上绘制的y轴起点
     * @return
     */
    public static BufferedImage drawImage(String backgroundPath, BufferedImage zxingImage, int x, int y) throws IOException {
        //读取背景图的图片流
        BufferedImage backgroundImage;
        //Try-with-resources 资源自动关闭,会自动调用close()方法关闭资源,只限于实现Closeable或AutoCloseable接口的类
        try (InputStream imagein = new FileInputStream(backgroundPath)) {
            backgroundImage = ImageIO.read(imagein);
        }
        return drawImage(backgroundImage, zxingImage, x, y);
    }


    /**
     * 将图片绘制在背景图上
     *
     * @param backgroundImage 背景图
     * @param zxingImage      图片
     * @param x               图片在背景图上绘制的x轴起点
     * @param y               图片在背景图上绘制的y轴起点
     * @return
     * @throws IOException
     */
    public static BufferedImage drawImage(BufferedImage backgroundImage, BufferedImage zxingImage, int x, int y) throws IOException {
        Objects.requireNonNull(backgroundImage, ">>>>>背景图不可为空");
        Objects.requireNonNull(zxingImage, ">>>>>二维码不可为空");
        //二维码宽度+x不可以超过背景图的宽度,长度同理
        System.out.println(backgroundImage.getWidth());
        System.out.println(backgroundImage.getHeight());
        if ((zxingImage.getWidth() + x) > backgroundImage.getWidth() || (zxingImage.getHeight() + y) > backgroundImage.getHeight()) {
            throw new IOException(">>>>>二维码宽度+x不可以超过背景图的宽度,长度同理");
        }

        //合并图片
        Graphics2D g = backgroundImage.createGraphics();
        g.drawImage(zxingImage, x, y,
                zxingImage.getWidth(), zxingImage.getHeight(), null);
        return backgroundImage;
    }

    /**
     * 将文字绘制在背景图上
     *
     * @param backgroundImage 背景图
     * @param x               文字在背景图上绘制的x轴起点
     * @param y               文字在背景图上绘制的y轴起点
     * @return
     * @throws IOException
     */
    public static BufferedImage drawString(BufferedImage backgroundImage, String text, int x, int y, Font font, Color color) {
        //绘制文字
        Graphics2D g = backgroundImage.createGraphics();
        //设置颜色
        g.setColor(color);
        //消除锯齿状
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //设置字体
//        g.setFont(font);
        //绘制文字
        g.drawString(text, x, y);
        return backgroundImage;
    }


    public static InputStream bufferedImageToInputStream(BufferedImage backgroundImage) throws IOException {
        return bufferedImageToInputStream(backgroundImage, "png");
    }

    /**
     * backgroundImage 转换为输出流
     *
     * @param backgroundImage
     * @param format
     * @return
     * @throws IOException
     */
    public static InputStream bufferedImageToInputStream(BufferedImage backgroundImage, String format) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try (
                ImageOutputStream
                        imOut = ImageIO.createImageOutputStream(bs)) {
            ImageIO.write(backgroundImage, format, imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());
            return is;
        }
    }

    /**
     * 保存为文件
     *
     * @param is
     * @param fileName
     * @throws IOException
     */
    public static void saveFile(InputStream is, String fileName) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(is);
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName))) {
            int len;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        }
    }

    public static void main(String[] args) {
        //二维码宽度
        int width = 50;
        //二维码高度
        int height = 50;
        //二维码内容
        String contcent = "整个二维码试试呗";
        BufferedImage zxingImage = null;
        try {
            //二维码图片流
            zxingImage = WatermarkUtil.enQRCode(contcent, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //背景图片地址
        String backgroundPath = "C:\\Users\\Mrlu\\Desktop\\1.png";
        InputStream inputStream = null;
        try {
            //合成二维码和背景图
            BufferedImage image = WatermarkUtil.drawImage(backgroundPath, zxingImage, 450, 350);
            //绘制文字
            //读取本地字体
            //Font font = new Font("微软雅黑", Font.BOLD, 35);
            //读取外部字体
//            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Windows\\Fonts\\msyhbd.ttc"));
//            //手动设置字体大小,粗体
//            font = font.deriveFont(35F).deriveFont(Font.BOLD);
            //文字内容
            String text = "17000";
//            image = Watermark.drawString(image, text, 375, 647,null,new Color(244,254,189));
            //图片转inputStream
            inputStream = WatermarkUtil.bufferedImageToInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保存的图片路径
        String originalFileName = "C:\\Users\\Mrlu\\Desktop\\111.png";
        try {
            //保存为本地图片
            WatermarkUtil.saveFile(inputStream, originalFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
