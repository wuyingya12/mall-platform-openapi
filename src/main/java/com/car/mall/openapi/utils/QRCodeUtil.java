package com.car.mall.openapi.utils;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

/** 
 * 二维码工具类 
 *  
 * @author hipiaoa
 *
 */
public class QRCodeUtil {  
	private static Logger logger = Logger.getLogger(QRCodeUtil.class);
    private static final String CHARSET = "utf-8";  
    private static final String FORMAT_NAME = "JPG";  
    // 二维码尺寸  
    private static final int QRCODE_SIZE = 370;  
    // LOGO宽度  
    private static final int WIDTH = 90;  
    // LOGO高度  
    private static final int HEIGHT = 90;  
  
    /**
     * @param content
     *            LOGO地址
     * @param needCompress
     * @return
     * @throws Exception
     */
    public static BufferedImage createImage(String content, InputStream inStream,
            boolean needCompress) throws Exception {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);  
        hints.put(EncodeHintType.MARGIN, 1);  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);  
        int width = bitMatrix.getWidth();  
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000  
                        : 0xFFFFFFFF);  
            }  
        }  
        if (inStream == null) {
            return image;  
        }  
        try{
	        // 插入图片  
	        QRCodeUtil.insertImage(image, inStream, needCompress);
        }catch(Exception e){
        	logger.error("--------------:图片格式不正确");
        }
        return image;  
    }  
  
    
    /**
     * @param content
     *            LOGO地址
     * @param needCompress
     * @return
     * @throws Exception
     */
    public static BufferedImage createImage(String content, InputStream inStream,int size,
            boolean needCompress) throws Exception {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);  
        hints.put(EncodeHintType.MARGIN, 1);  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                BarcodeFormat.QR_CODE, size, size, hints);  
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000  
                        : 0xFFFFFFFF);  
            }  
        }  
        if (inStream==null) {
            return image;  
        }  
        try{
	        // 插入图片  
	        QRCodeUtil.insertImage(image, inStream, needCompress);
        }catch(Exception e){
        	logger.error("--------------:图片格式不正确");
        }
        return image;  
    }  
    
    /** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片
     *            LOGO图片地址 
     * @param needCompress 
     *            是否压缩 
     * @throws Exception 
     */  
    private static void insertImage(BufferedImage source, InputStream inStream,
            boolean needCompress) throws Exception {  
        Image src = ImageIO.read(inStream);
        int width = src.getWidth(null);  
        int height = src.getHeight(null);  
        if (needCompress) { // 压缩LOGO  
            if (width > WIDTH) {  
                width = WIDTH;  
            }  
            if (height > HEIGHT) {  
                height = HEIGHT;  
            }  
            Image image = src.getScaledInstance(width, height,  
                    Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }  
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        int x = (QRCODE_SIZE - width) / 2;  
        int y = (QRCODE_SIZE - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 22, 22);  
        graph.setStroke(new BasicStroke(4f));  
        graph.draw(shape);  
        graph.dispose();  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param destPath
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, InputStream inStream, String destPath,
            boolean needCompress) throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, inStream,
                needCompress);  
        //FileUtils.mkdirs(destPath);
        File distFile = new File(destPath);
		if(!distFile.getParentFile().exists()){
			distFile.mkdir();
		}
        ImageIO.write(image, FORMAT_NAME, new File(destPath));  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容
     *            LOGO地址 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static void encode(String content, InputStream inStream, String destPath)
            throws Exception {  
        QRCodeUtil.encode(content, inStream, destPath, false);
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath,  
            boolean needCompress) throws Exception {  
        QRCodeUtil.encode(content, null, destPath, needCompress);
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath) throws Exception {  
        QRCodeUtil.encode(content, null, destPath, false);
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param inStream
     *            LOGO地址 
     * @param output 
     *            输出流 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, InputStream inStream,
            OutputStream output, boolean needCompress) throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, inStream,
                needCompress);  
        ImageIO.write(image, FORMAT_NAME, output);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param output 
     *            输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output)  
            throws Exception {  
        QRCodeUtil.encode(content, null, output, false);
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param file 
     *            二维码图片 
     * @return 
     * @throws Exception 
     */  
    public static String decode(File file) throws Exception {  
        BufferedImage image;  
        image = ImageIO.read(file);  
        if (image == null) {  
            return null;  
        }  
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
        Result result;  
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();  
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);  
        result = new MultiFormatReader().decode(bitmap, hints);  
        String resultStr = result.getText();  
        return resultStr;  
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param path 
     *            二维码图片地址 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String path) throws Exception {  
        return QRCodeUtil.decode(new File(path));
    }

    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 根据网络图片地址生成base64编码
     * @param imageUrl
     * @return
     */
    public static  String  imageUrlToBase64(String  imageUrl){
        byte[] bytes = getImageFromNetByUrl(imageUrl);
        StringBuffer imgbuffer=new StringBuffer("data:image/png;base64,");
        imgbuffer.append(BASE64Encoder.encode(bytes).trim());
        return imgbuffer.toString();
    }

//    public static void main(String[] args) throws Exception {
//    	 String imgPath1 = "D:\\123.jpg";
//    	    String imgPath = "D:\\ddaf1.jpg";
//        String text = "http://wx.vcfilm.cn/enterWeb?cinemaId=1062";
//        QRCodeUtil.encode(text, new File(imgPath1), imgPath, true);
//        BufferedImage image = QRCodeUtil.createImage(text, new File(imgPath1), true);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "png", baos);
//        byte[] bytes = baos.toByteArray();
//
//        System.out.println(BASE64Encoder.encode(bytes).trim());
//    }
}   
