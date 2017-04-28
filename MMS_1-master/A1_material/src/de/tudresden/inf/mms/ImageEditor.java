
package de.tudresden.inf.mms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * @author <Dominic> <Sehring>, <4050898>
 * 
 */
public class ImageEditor {

	/**
	 * Originalbild (Ausgangspunkt für Berechnungen)
	 */
	private BufferedImage image;

	/**
	 * Temporäre Kopie bzw. Ergebnisbild
	 */
	private BufferedImage tmpImg;

	public ImageEditor(BufferedImage image) {
		super();
		this.image = image;
		tmpImg = new BufferedImage(image.getWidth(), image.getHeight(),
				image.getType());
	}

	/**
	 * Konvertiert das vorgegebene RGB-Bild ({@link ImageEditor#image}) in ein
	 * Graustufenbild.
	 * 
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image grayscale() {
		int gray;

		int[] rgb;

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
				gray = (int) Math.floor((rgb[0] + rgb[1] + rgb[2]) / 3);
				rgb[0] = gray;
				rgb[1] = gray;
				rgb[2] = gray;
				tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
			}
		}

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image invert() {

		/*
		 * ToDo
		 */
		int[] rgb;
		
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            	rgb[0] = 255 - rgb[0];
            	rgb[1] = 255 - rgb[1];
            	rgb[2] = 255 - rgb[2];
                tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
            }
        }

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image rotate() {

		/*
		 * ToDo
		 */
		AffineTransform atrans = new AffineTransform();
		atrans.translate(image.getWidth() / 2, image.getHeight() / 2);
		atrans.rotate(Math.PI);
		atrans.translate(-image.getWidth()/2, -image.getHeight()/2);
        
		Graphics2D g2d = tmpImg.createGraphics();
		g2d.drawImage(image, atrans, null);
        
        
        return tmpImg;
	}

	/**
	 * @see Aufgabe 1.1.3
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image linHist() {

		/*
		 * ToDo
		 */
		int[] rgb;
		int[] helligkeit = new int[256];
		
		for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            
	            double yy = (0.299 * rgb[0]) + (0.587 * rgb[1]) + (0.114 * rgb[2]);
	            helligkeit[(int)yy] += 1; 
	
            }
		}
		
		double kwh = (double)255 / (image.getWidth() * image.getHeight());
		int[] fa = new int[256];
		int fwert = 0;
		
		for (int a = 0; a < 256; a++) {
			
			fwert += helligkeit[a];
			fa[a] = (int)(kwh * fwert);
			
		}
		
		for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            	
            	double yy = (0.299 * rgb[0]) + (0.587 * rgb[1]) + (0.114 * rgb[2]);
            	double y1 = fa[(int)yy];
            	double cb = 128 - (0.168736 * rgb[0]) - (0.331264*rgb[1]) + (0.5 * rgb[2]);
            	double cr = 128 + (0.5 * rgb[0]) - (0.418688 * rgb[1]) - (0.081312 * rgb[2]);
            	
            	rgb[0] = (int)Math.floor(y1 + 1.402 * (cr - 128));
            	rgb[1] = (int)Math.floor(y1 - 0.34414 * (cb - 128) - 0.71414 * (cr - 128));
            	rgb[2] = (int)Math.floor(y1 + 1.772 * (cb - 128)); 
            	
            	tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
            } 
		}
		return tmpImg; 
		
	}
	
	/**
	 * @see Aufgabe 1.2.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image subsampling() {

		/*
		 * ToDo
		 */
		
		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x = x + 2) {
            for (int y = 0; y < image.getHeight(); y = y + 2) {
            	
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            	tmpImg.setRGB(x, y, ImageHelper.toIntRGB(rgb));
            	tmpImg.setRGB(x+1, y, ImageHelper.toIntRGB(rgb));
            	tmpImg.setRGB(x, y+1, ImageHelper.toIntRGB(rgb));
            	tmpImg.setRGB(x+1, y+1, ImageHelper.toIntRGB(rgb));
            	            	
            }
		}
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.2.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image colorSubsampling() {

		/*
		 * ToDo
		 */
		int[] pixel0 = new int[3];
		int[] pixel1 = new int[3];
		int[] pixel2 = new int[3];
		int[] pixel3 = new int[3];
		
		for (int x = 0; x < image.getWidth(); x = x + 2) {
            for (int y = 0; y < image.getHeight(); y = y + 2) {
            	           
            	convertToYCbCr(ImageHelper.toRGBArray(image.getRGB(x, y)), pixel0);
            	convertToYCbCr(ImageHelper.toRGBArray(image.getRGB(x+1, y)), pixel1);
            	convertToYCbCr(ImageHelper.toRGBArray(image.getRGB(x, y+1)), pixel2);
            	convertToYCbCr(ImageHelper.toRGBArray(image.getRGB(x+1, y+1)), pixel3);
            	
            	int cb = (pixel0[1] + pixel1[1] + pixel2[1] + pixel3[1]) / 4;
            	int cr = (pixel0[2] + pixel1[2] + pixel2[2] + pixel3[2]) / 4;
            	            	            	            	
            	int[] cache = new int[3];
            
            	tmpImg.setRGB(x, y, convertToRGB(pixel0[0], cb, cr, cache));
            	tmpImg.setRGB(x+1, y, convertToRGB(pixel1[0], cb, cr, cache));
            	tmpImg.setRGB(x, y+1, convertToRGB(pixel2[0], cb, cr, cache));
            	tmpImg.setRGB(x+1, y+1, convertToRGB(pixel3[0], cb, cr, cache));
            	
            }
		}
		
		
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.1
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image colorQuant() {

		/*
		 * ToDo
		 */
		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            	tmpImg.setRGB(x, y, ImageHelper.toIntRGB(ImageHelper.getColorFromPalette(rgb)));
            }
		}
		

		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.2
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image orderedDithering() {

		/*
		 * ToDo
		 */

		int[] rgb;
		
		for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	
            	int[] farbwert;
            	
            	rgb = ImageHelper.toRGBArray(image.getRGB(x, y));
            	farbwert = ImageHelper.toRGBArray(ImageHelper.BAYER8x8[x % 8][y % 8]);
            	
            	for (int i = 0; i < rgb.length; i++)
            		rgb[i] += farbwert[i];

            	tmpImg.setRGB(x, y, ImageHelper.toIntRGB(ImageHelper.getColorFromPalette(rgb)));
            	
            }
		}
		
		return tmpImg;
	}

	/**
	 * @see Aufgabe 1.3.3
	 * @return {@link java.awt.image.BufferedImage}
	 */
	public Image floydSteinbergDithering() {

		/*
		 * ToDo
		 */
		
		Graphics g = tmpImg.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
       
        int rgb[]; 
 
        int w = image.getWidth() - 1;
        int h = image.getHeight() - 1;
 
        for (int y = 0; y < h; y++){
            for (int x = 0; x < w; x++){
               
                rgb = ImageHelper.toRGBArray(tmpImg.getRGB(x, y));
               
                int[] newPixel = ImageHelper.getColorFromPalette(rgb);        
               

                int quantErrorR = rgb[0] - newPixel[0];
                int quantErrorG = rgb[1] - newPixel[1];
                int quantErrorB = rgb[2] - newPixel[2];
               
                tmpImg.setRGB(x, y, ImageHelper.toIntRGB(newPixel));
               
               
                if(x < w)
                {
                    tmpImg.setRGB(x + 1, y, ImageHelper.toIntRGB(quantisierungError(x + 1, y, quantErrorR, quantErrorG, quantErrorB, 7)));
                }
                if(x > 1 && y< h){
                	tmpImg.setRGB(x - 1, y + 1, ImageHelper.toIntRGB(quantisierungError(x - 1, y + 1, quantErrorR, quantErrorG, quantErrorB, 3)));
                }
                if(y < h){
                	tmpImg.setRGB(x, y + 1, ImageHelper.toIntRGB(quantisierungError(x, y + 1, quantErrorR, quantErrorG, quantErrorB, 5)));
                }
                if(x < w && y < h){
                	tmpImg.setRGB(x + 1, y + 1, ImageHelper.toIntRGB(quantisierungError(x + 1, y + 1, quantErrorR, quantErrorG, quantErrorB, 1)));
                }
               
            }
        }


		return tmpImg;
	}
	
	public void convertToYCbCr(int[] rgb, int[] YCbCr)
	{
        
    	YCbCr[0] = (int)(Math.floor((0.299 * rgb[0]) + (0.587 * rgb[1]) + (0.114 * rgb[2])));
    	YCbCr[1] = (int)(Math.floor(128 - (0.168736 * rgb[0]) - (0.331264*rgb[1]) + (0.5 * rgb[2])));
    	YCbCr[2] = (int)(Math.floor(128 + (0.5 * rgb[0]) - (0.418688 * rgb[1]) - (0.081312 * rgb[2])));
    	    	
	}
	

    public int convertToRGB(int y, int cb, int cr, int[] rgb){
            
    	rgb[0] = (int)(y + 1.402 * (cr - 128));
    	rgb[1] = (int)(y - 0.34414 * (cb - 128) - 0.71414 * (cr -128));
    	rgb[2] = (int)(y + 1.772 * (cb - 128));
            
    	return ImageHelper.toIntRGB(rgb);
    }
        
    public int[] quantisierungError(int x, int y, int quantErrorR, int quantErrorG, int quantErrorB, int quantGewicht)
    {
        int[] pixel = ImageHelper.toRGBArray(tmpImg.getRGB(x, y));
        pixel[0] += quantErrorR * quantGewicht / 16;
        pixel[1] += quantErrorG * quantGewicht / 16;
        pixel[2] += quantErrorB * quantGewicht / 16;
       
        return pixel;
    }

}
