package uk.ac.soton.ecs.pb2g19.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The type My hybrid images.
 */
public class MyHybridImages {
    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage  the image to which apply the low pass filter
     * @param lowSigma  the standard deviation of the low-pass filter
     * @param highImage the image to which apply the high pass filter
     * @param highSigma the standard deviation of the low-pass component of computing the high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        MBFImage highFreqImage;
        MBFImage lowFreqImage;

        int size = (int) (8.0f * lowSigma + 1.0f); // this implies the window is +/- 4 sigmas from the centre of the Gaussian
        if (size % 2 == 0) size++; // size must be odd

        // Guassian2D is used to get the kernel needed in MyConvolution constructor
        FImage kernelImage = Gaussian2D.createKernelImage(size, lowSigma);
        float[][] pixelArray = kernelImage.pixels;
        uk.ac.soton.ecs.pb2g19.hybridimages.MyConvolution lowConvolution = new uk.ac.soton.ecs.pb2g19.hybridimages.MyConvolution(pixelArray);
        lowFreqImage = lowImage.process(lowConvolution);

        size = (int) (8.0f * highSigma + 1.0f);
        if (size % 2 == 0) size++;
        kernelImage = Gaussian2D.createKernelImage(size, highSigma);
        pixelArray = kernelImage.pixels;
        uk.ac.soton.ecs.pb2g19.hybridimages.MyConvolution highConvolution = new uk.ac.soton.ecs.pb2g19.hybridimages.MyConvolution(pixelArray);
        highFreqImage = highImage.subtract(highImage.process(highConvolution));

        return lowFreqImage.add(highFreqImage);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        MBFImage highImage1 = ImageUtilities.readMBF(new File("cat.bmp"));
        MBFImage lowImage1 = ImageUtilities.readMBF(new File("dog.bmp"));
                MBFImage convolutionImage = MyHybridImages.makeHybrid(lowImage1, 4,highImage1,10);
        DisplayUtilities.display(convolutionImage);

//        MBFImage highImage2 = ImageUtilities.readMBF(new File("jim.png"));
//        MBFImage lowImage2 = ImageUtilities.readMBF(new File("will.jpg"));
//        MBFImage convolutionImage = MyHybridImages.makeHybrid(lowImage2, 4,highImage2,15);
//        DisplayUtilities.display(convolutionImage);


        try {
            // retrieve image
            File outputFile = new File("convolution2.PNG");
            ImageIO.write(ImageUtilities.createBufferedImage(convolutionImage), "PNG", outputFile);
        } catch (IOException e) {

        }
    }
}
