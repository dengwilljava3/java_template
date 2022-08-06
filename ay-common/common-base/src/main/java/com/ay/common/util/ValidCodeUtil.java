package com.ay.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class ValidCodeUtil {
	
	public static void main(String[] args) throws Exception {
		int width = 150;
		int height = 50;
		FileOutputStream output = new FileOutputStream("D:\\a.jpg");
		generateImage(width, height, output);
	}

	public static void generateImage(int width, int height, OutputStream out) throws Exception, FileNotFoundException {
		// 在内存中生成图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// 绘制图片边框,fillrect是生成一个实心方框，drawrect是生成空心方框
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, width, height);
		// width-1表示减少一个像素，这样才能使线条显示出来，不至于与背景重叠
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, width - 1, height - 1);
		String str = "1234567890";
		Random random = new Random();
		saveString(width, height, str, random, g);
		saveLine(width, height, random, g);
		saveImage(image, "jpg", out);
		System.out.println("生成验证码成功！");
	}

	private static void saveImage(BufferedImage img, String jpg, OutputStream out) throws Exception {
		ImageIO.write(img, "JPEG", out);
	}

	// 随机生成颜色
	private static Color getrandomColor(Random random) {
		int colorIndex = random.nextInt(4);
		switch (colorIndex) {
		case 0:
			return Color.BLUE;
		case 1:
			return Color.BLACK;
		case 2:
			return Color.GREEN;
		case 3:
			return Color.red;
		case 4:
			return Color.DARK_GRAY;
		default:
			return Color.YELLOW;
		}
	}

	// 绘制验证码的字符
	private static void saveString(int width, int height, String str, Random random, Graphics graphics) {
		for (int i = 0; i < 4; i++) {
			// 获取随机的字符
			int index = random.nextInt(str.length());
			char ch = str.charAt(index);
			// 获取随机颜色
			Color color = getrandomColor(random);
			graphics.setColor(color);
			// 设置字体
			Font font = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, height / 2);
			graphics.setFont(font);
			// 写入验证码
			graphics.drawString(ch + "", (i == 0) ? width / 4 * i + 2 : width / 4 * i, height - height / 4);
		}
	}

	// 绘制干扰线
	private static void saveLine(int width, int height, Random random, Graphics graphics) {
		Color lineColor = getrandomColor(random);
		for (int i = 0; i < 10; i++) {
			int x1 = random.nextInt(width);
			int x2 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int y2 = random.nextInt(height);
			Color randomColor = lineColor;
			graphics.setColor(randomColor);
			graphics.drawLine(x1, x2, y1, y2);
		}
	}

}
