import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;


/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class GUI extends JFrame
{
    private JPanel rootPanel;
    private JButton buttonTrain;
    private JPanel imagePanel;
    private JLabel imageLabel;
    private JLabel labelError;
    private JLabel labelAnswer;
    private JLabel labelEpoch;
    private JTextArea textAreaAnswer;
    private JButton buttonTest;
    private NeuralNet neuralNet;

    public GUI()
    {
        setContentPane(rootPanel);
        pack();
        setTitle("Обучение через дельта-правило");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        buttonTest.setEnabled(false);

        buttonTrain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                train();
            }
        });
        buttonTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
    }

    public void train()
    {
        try
        {
            buttonTest.setEnabled(false);
            Vector[] trainVectorSet = readTrainVectors("C://Train");
            neuralNet = new NeuralNet(trainVectorSet[0].getX().length, trainVectorSet[0].getDisireOutputs().length);
            neuralNet.setComplete(false);

            Runnable task1 = () -> { try { neuralNet.train(trainVectorSet); } catch (InterruptedException e) {} };
            Thread thread1 = new Thread(task1);
            Runnable task2 = () -> {
                while (!neuralNet.isComplete())
                {
                    labelEpoch.setText("Номер эпохи: " + neuralNet.getEpochNumber());
                    labelError.setText("Ошибки нейронов: " + Arrays.toString(neuralNet.getError()));
                }
                textAreaAnswer.append("Обучение завершено\n");
                buttonTest.setEnabled(true);
            };
            Thread thread2 = new Thread(task2);

            thread1.start();
            thread2.start();
        }
        catch (IOException e)
        {
            showMessageDialog(null, "Файл не найден");
        }
        catch (Exception e)
        {
            showMessageDialog(null, e.toString());
        }

        int f = 0;
    }

    public void test() {
        try
        {
            String path = "c://Test//";
            for (int i = 0; i < 6; i++)
            {
                File[] files = new File(path + i).listFiles();
                for (File file : files)
                {
                    double[] testVector = readVector(file.getPath());
                    double[] answer = neuralNet.test(testVector);
                    textAreaAnswer.append(String.format("Тест-образ №%d = %s;%n", i, Arrays.toString(answer)));
                    //textAreaAnswer.setCaretPosition(textAreaAnswer.getDocument().getLength());
                }
            }
        }
        catch (IOException e)
        {
            showMessageDialog(null, "Файл не найден");
        }
        catch (Exception e)
        {
            showMessageDialog(null, e.toString());
        }
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

        for (int i = 0; i < 6; i++)
        {
            File[] files = new File(rootDir + "//" + i).listFiles();
            for (File file : files)
            {
                BufferedImage image = ImageIO.read(file);

                int[][] grayImage = imageToGrayScale(image);
                double[] imageVector = imageToVector(grayImage);

                double[] desireOutputs = new double[6];
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
