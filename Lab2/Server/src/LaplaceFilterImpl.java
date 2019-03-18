import image.Pixel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class LaplaceFilterImpl implements LaplaceFilter {
    private int[][] FILTER =
                {   {0,  1, 0},
                    {1,  -4, 1},
                    {0,  1, 0}  };
    private final int ADD_TO_WIDTH =  1;
    private final int ADD_TO_HEIGHT = 1;

    protected LaplaceFilterImpl() {
    }

    /**
     * This method selects a contour using the Laplace operator.
     * @param image source srcImage
     * @return image with a selected contours
     */
    @Override
    public byte[] transform(byte[] image)  {
        try{
            BufferedImage srcImage = ImageIO.read(new ByteArrayInputStream(image));
        /*
        Transform the original image to gray-type image.
         */
            BufferedImage scaled = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = scaled.createGraphics();
            graphics.drawImage(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), null);
            graphics.dispose();
            srcImage = scaled;
            BufferedImage destImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());

            Pixel[][] pix = convertToPixelsAndExpand(srcImage);

            Pixel current;
        /*
            On i,j-step we work with (i+FILTER.size/2),(j+FILTER.size/2) pixel.
         */
            for (int i = 0; i < srcImage.getWidth(); i++) {
                for (int j = 0; j < srcImage.getHeight(); j++) {
                    int r = 0, g = 0, b = 0, o = 0;
                    for (int k = 0; k < FILTER.length; k++) {
                        for (int l = 0; l < FILTER[k].length; l++) {
                            current = pix[i + k][j + l];
                            r += current.getRed() * FILTER[k][l];
                            g += current.getGreen() * FILTER[k][l];
                            b += current.getBlue() * FILTER[k][l];
                            o += current.getOpacity() * FILTER[k][l];
                        }
                    }
                    Pixel p = new Pixel(checkAndInversion(r),
                            checkAndInversion(g),
                            checkAndInversion(b),
                            checkAndInversion(o));
                    destImage.setRGB(i, j, p.getRGB());
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(destImage, "png", os);
            return os.toByteArray();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This method convert image to pixels-array and expand image
     * by adding a frame to it. Size of frame = FilterSize / 2
     * @param image - source image
     * @return pixels-array. and each element of it - pixel of source image
     */
    private Pixel[][] convertToPixelsAndExpand(BufferedImage image) {
        int w = image.getWidth(),
            h = image.getHeight();
        int width = w + 2 * ADD_TO_WIDTH,
            height = h + 2 * ADD_TO_HEIGHT;

        Pixel[][] pix = new Pixel[width][height];

        int x,y;
        /*
            Calculate XY-coordinate of pixel in source image
            and write this pixel to expanded pixel-array.
        */
        for (int i = 0; i < width; i++) {
            x = Math.max(0, i - ADD_TO_WIDTH);
            x = Math.min(x, w - 1);
            for (int j = 0; j < height; j++) {
                y = Math.max(0, j - ADD_TO_HEIGHT);
                y = Math.min(y, h - 1);
                pix[i][j] = new Pixel(image.getRGB(x, y));
            }
        }
        return pix;
    }

    /**
     * This method checks the color for belonging to the interval [0, 255],
     * and return inverted color.
     * @param color the integer representation of the color
     * @return inverted color
     */
    private byte checkAndInversion(int color){
        if (color < 0)
            return (byte) 255;
        if (color > 255)
            return 0;
        return (byte) (255 - color);
    }
}
