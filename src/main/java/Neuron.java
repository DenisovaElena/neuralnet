import java.util.Random;

/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class Neuron
{
    private double[] weight; //вектор синаптических весов
    private double out; //выход нейрона
    public static Random random = new Random(); // объявляем и создаем экземпляр класса генератора случайных значений
    public static final double rangeMin = -0.3; //нижняя граница генератора случайных весов
    public static final double rangeMax = 0.3; //верхняя граница генератора случайных весов

    public Neuron(int weightsCount) {
        this.weight = new double[weightsCount];
        this.out = 0.0;
        randomizeWeights();
    }

    public void randomizeWeights()
    {
        for (int i = 0; i < weight.length; i++)
        {
            weight[i] = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        }
    }

    private double activationFunc(double val)
    {
        if (val >= 0)
            return 1.0;
        else
            return 0.0;
    }

    public void calcOut(double[] x)
    {
        double sum = 0;
        for (int i = 0; i < x.length; i++)
        {
            sum += x[i]*weight[i];
        }
        this.out = activationFunc(sum);
    }

    public double getOut()
    {
        return out;
    }

    public void correctWeights(double[] deltaWeight)
    {
        for (int i = 0; i < weight.length; i++)
        {
            weight[i] += deltaWeight[i];
        }
    }

    public double[] getWeight()
    {
        return weight;
    }
}
