package com.markuswi.fontpreviewimagecreator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FontPreviewImageCreator {

	private FontPreviewImageCreator(){}
	
	private static Font createFont(InputStream inputStream) throws FontFormatException, IOException {
		return Font.createFont(Font.TRUETYPE_FONT, inputStream);
	}
	
	private static BufferedImage createBufferedImagePlaceholder() {
		return new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
	}
	
	private static Graphics2D getGraphicsContext(BufferedImage bufferedImage, Font font, float fontSize) {
		Graphics2D graphics2d = bufferedImage.createGraphics();
		graphics2d.setFont(font.deriveFont(fontSize));
		return graphics2d;
	}
	
	private static Rectangle getPixelBounds(String text, Graphics2D graphics2d) {
		FontRenderContext fontRenderContext = graphics2d.getFontRenderContext();
		GlyphVector glyphVector = graphics2d.getFont().createGlyphVector(fontRenderContext, text);
		return glyphVector.getPixelBounds(graphics2d.getFontRenderContext(), 0, 0);
	}
	
	private static BufferedImage createBufferedImageWithText(Font font, String text, float fontSize, Color fontColor) {
		BufferedImage bufferedImagePlaceHolder = createBufferedImagePlaceholder();
		Graphics2D graphics2d = getGraphicsContext(bufferedImagePlaceHolder, font, fontSize);
		Rectangle pixelBoundsRectangle = getPixelBounds(text, graphics2d);
		BufferedImage bufferedImageResult = new BufferedImage((int)pixelBoundsRectangle.getWidth(), (int)pixelBoundsRectangle.getHeight()*2, BufferedImage.TYPE_INT_ARGB);
		graphics2d = getGraphicsContext(bufferedImageResult, font, fontSize);
		graphics2d.setColor(fontColor);
		graphics2d.drawString(text, 0, Double.valueOf((bufferedImageResult.getHeight() - pixelBoundsRectangle.getHeight()) / 2 + pixelBoundsRectangle.getHeight()).intValue());
		graphics2d.create();
		return bufferedImageResult;
	}
	
	private static BufferedImagePixelBounds getBufferedImagePixelBounds(BufferedImage bufferedImage) {
		BufferedImagePixelBounds bufferedImagePixelBounds = new BufferedImagePixelBounds();
		for (int y = 0; y < bufferedImage.getHeight(); y++) {
			for (int x = 0; x < bufferedImage.getWidth(); x++) {

				if (bufferedImage.getRGB(x, y) != 0) {
					if (bufferedImagePixelBounds.getMinY() == -1) {
						bufferedImagePixelBounds.setMinY(y);
					}
					if (x > bufferedImagePixelBounds.getMaxX()) {
						bufferedImagePixelBounds.setMaxX(x);
					}
					if (y > bufferedImagePixelBounds.getMaxY()) {
						bufferedImagePixelBounds.setMaxY(y);
					}
				}

			}
		}
		return bufferedImagePixelBounds;
	}
	
	private static BufferedImage cropBufferedImage(BufferedImage bufferedImage) {
		BufferedImagePixelBounds bufferedImagePixelBounds = getBufferedImagePixelBounds(bufferedImage);
		return bufferedImage.getSubimage(bufferedImagePixelBounds.getX(), bufferedImagePixelBounds.getY(), bufferedImagePixelBounds.getWidth(), bufferedImagePixelBounds.getHeight());
	}
	
	private static BufferedImage createFontPreviewImage(Font font, String previewText, float fontSize, Color fontColor) {
		BufferedImage bufferedImage = createBufferedImageWithText(font, previewText, fontSize, fontColor);
		return cropBufferedImage(bufferedImage);
	}
	
	public static BufferedImage createFontPreviewImage(File fontFile, String previewText, float fontSize, Color fontColor) throws FontFormatException, IOException {
		Font font = createFont(new FileInputStream(fontFile));
		return createFontPreviewImage(font, previewText, fontSize, fontColor);
	}
	
	public static BufferedImage createFontPreviewImage(String nameOfFileFromClassPath, String previewText, float fontSize, Color fontColor) throws FontFormatException, IOException {
		Font font = createFont(FontPreviewImageCreator.class.getClassLoader().getResource(nameOfFileFromClassPath).openStream());
		return createFontPreviewImage(font, previewText, fontSize, fontColor);
	}
	
}
