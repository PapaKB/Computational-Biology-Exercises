package uk.ac.soton.ecs.pb2g19.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

/**
 * A class that performs convolution.
 */
public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private float[][] kernel;

    /**
     * Instantiates a new My convolution.
     *
     * @param kernel the kernel
     */
    public MyConvolution(float[][] kernel) {
        //note that like the image pixels kernel is indexed by [row][column]
        this.kernel = kernel;
    }

    @Override
    /**
     * Convolve image with kernel to generate new image
     * @param image the image to convolve the kernel with
     * */
    public void processImage(FImage image) {
        int kernelWidth = this.kernel[0].length;
        int kernelHeight = this.kernel.length;
        //The number of rows and columns of kernel both need to be odd
        if(kernelHeight % 2 != 0 && kernelWidth % 2 != 0) {
            // After a kernel is applied to an image the output is smaller.
            // So to preserve the image size padding is added.
            int widthPadding = (kernelWidth-1)/2;
            int heightPadding = (kernelHeight-1)/2;

            FImage paddedImage = image.padding(widthPadding, heightPadding, (float)0);


            float[][] bufferImage = new float[image.getHeight()][image.getWidth()];
            for(int y=0; y<image.getHeight(); y++) {
                for (int x = 0; x<image.getWidth(); x++) {
                    FImage extract = paddedImage.extractROI(x, y, kernelWidth, kernelHeight);
                    bufferImage[y][x] = convolve(extract);
                }
            }
            image.internalAssign(new FImage(bufferImage));
        }
    }

    /**
     * Performs the convolution with an image and kernel
     *
     * @param image the image to convolve with
     * @return the float computed from convolution
     */
    private float convolve(FImage image) {
        float pixelValue = 0;
        for(int y=0; y< this.kernel.length; y++) {
            for(int x=0; x<this.kernel[0].length; x++) {
                pixelValue += (kernel[y][x] * image.getPixel(x, y));
            }
        }
        return pixelValue;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) { }
}
