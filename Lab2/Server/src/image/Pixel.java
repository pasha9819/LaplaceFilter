package image;

public class Pixel{
    private final int rgb;

    public Pixel(int red, int green, int blue, int opacity) {
        rgb = (opacity << 24 & 0xFF000000) | (red << 16 & 0xFF0000)| (green << 8 & 0xFF00)| (blue & 0xFF) ;
    }

    public Pixel(int rgb) {
        this.rgb = rgb;
    }

    public Pixel(Pixel pixel){
        rgb = pixel.rgb;
    }

    public int getRGB(){
        return rgb;
    }

    public int getOpacity(){
        return (rgb & 0xFF000000) >> 24;}

    public int getRed(){
        return (rgb & 0xFF0000) >> 16;
    }

    public int getGreen(){
        return (rgb & 0xFF00) >> 8;
    }

    public int getBlue(){
        return rgb & 0xFF;
    }
}
