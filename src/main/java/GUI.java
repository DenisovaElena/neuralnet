import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class GUI extends JFrame
{
    private JPanel rootPanel;
    private JButton button1;
    private JPanel imagePanel;

    public GUI()
    {
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readImages();
            }
        });
    }

    public void readImages()
    {
        String rootDir = "C://MNIST";
        try
        {
            for (int i = 0; i < 10; i++)
            {
                File[] files = new File(rootDir + "//" + i).listFiles();
                for (File file : files)
                {
                    BufferedImage image = ImageIO.read(file);
                    imagePanel.paint(image.createGraphics());
                    /*
                    int[][] grayImage = imageToGrayScale(image);

                    int height = grayImage[0].length;
                    int width = grayImage[1].length;
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            Color c = new Color(grayImage[x][y], grayImage[x][y], grayImage[x][y], 0);
                            image.setRGB(x, y, c.getRGB());
                        }
                    }

                    imagePanel.paint(image.createGraphics());
                    */
                    break;
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public int[][] imageToGrayScale(BufferedImage image)
    {
        int[][] resultImage = new int[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                Color c = new Color(image.getRGB(x, y));
                resultImage[x][y] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
            }
        }
        return resultImage;
    }
    public double[] imageToVector(double[][] image)
    {
        double[] resultVector = new double[image[0].length * image[1].length];
        int i = 0;
        for(int x = 0; x < image.length; x++)
        {
            for (int y = 0; y < image.length; y++)
            {
                resultVector[i++] = image[x][y];
            }
        }
        return resultVector;
    }

}
