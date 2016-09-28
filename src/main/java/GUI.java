import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;


/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class GUI extends JFrame
{
    private JPanel rootPanel;
    private JButton button1;
    private JPanel imagePanel;
    private JLabel imageLabel;

    public GUI()
    {
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launch();
            }
        });
    }

    public void launch()
    {

        try
        {
            Vector[] trainVectorSet = readTrainVectors("C://MNIST");
            NeuralNet neuralNet = new NeuralNet(trainVectorSet[0].getX().length, trainVectorSet[0].getDisireOutputs().length);
            neuralNet.train(trainVectorSet);
            showMessageDialog(null, "Ответ: " + neuralNet.test(readVector("C://MNIST//1/1_100.bmp")).toString());

        }
        catch (Exception e)
        {
            showMessageDialog(null, e.toString());
        }

        int f = 0;
    }

    public double[] readVector(String path) throws IOException
    {
        BufferedImage image = ImageIO.read(new File(path));
        int[][] grayImage = imageToGrayScale(image);
        double[] imageVector = imageToVector(grayImage);
        return imageVector;


    }


    public Vector[] readTrainVectors(String rootDir) throws IOException
    {
        List<Vector> trainVectorSet = new ArrayList();

        for (int i = 0; i < 10; i++)
        {
            File[] files = new File(rootDir + "//" + i).listFiles();
            for (File file : files)
            {
                BufferedImage image = ImageIO.read(file);

                int[][] grayImage = imageToGrayScale(image);
                double[] imageVector = imageToVector(grayImage);

                double[] desireOutputs = new double[10];
                for (int k = 0; k < desireOutputs.length; k++)
                {
                    desireOutputs[k] = i == k ? 1 : 0;
                }

                trainVectorSet.add(new Vector(imageVector, desireOutputs));
                //imageLabel.setIcon(new ImageIcon(image));
            }
        }
        return (Vector[])trainVectorSet.toArray(new Vector[trainVectorSet.size()]);
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

    public BufferedImage grayScaleToImage(int[][] grayImage)
    {
        int height = grayImage[0].length;
        int width = grayImage[1].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                Color c = new Color(grayImage[x][y], grayImage[x][y], grayImage[x][y], 0);
                image.setRGB(x, y, c.getRGB());
            }
        }
        return image;
    }


    public double[] imageToVector(int[][] image)
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
