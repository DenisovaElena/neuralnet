import java.util.*;

/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class NeuralNet
{
    private int inputVectorSize;
    private Neuron[] layer;
    private int epochNumber;
    private volatile boolean complete;
    private double[] error;
    private double errorCommon;
    private final double eta = 0.00000001;
    private final double epsThreshold = 0.0001;

    public NeuralNet(int inputVectorSize, int outputNeuronsCount)
    {
        this.inputVectorSize = inputVectorSize;
        layer = new Neuron[outputNeuronsCount];
        for (int j = 0; j < outputNeuronsCount; j++)
        {
            layer[j] = new Neuron(inputVectorSize);
        }
        error = new double[layer.length];
        errorCommon = 0.0;
    }

    public double[] getError() {
        return error;
    }

    public double getErrorCommon()
    {
        return errorCommon;
    }

    public int getEpochNumber() {
        return epochNumber;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }

    public void train(Vector[] vectorSet) throws InterruptedException
    {
        epochNumber = 0;
        double[][] deltaWeight = new double[layer.length][];
        for (int j = 0; j < layer.length; j++)
        {
            // создаем пустой массив для каждого синпатического веса wij каждого j-го нейрона в слое
            deltaWeight[j] = new double[layer[j].getWeight().length];
        }
        // создаем пустой массив для хранения ошибки каждого j-го нейрона
        error = new double[layer.length];
        Random random = new Random();

        while (true)
        {

                int m = random.nextInt(vectorSet.length);
                // вычисляем выход каждого j-го нейрона
                for (int j = 0; j < layer.length; j++)
                {
                    layer[j].calcOut(vectorSet[m].getX());
                }


                // вычисляем ошибку каждого j-го нейрона

                errorCommon = 0.0;
                for (int j = 0; j < layer.length; j++)
                {
                    error[j] = (vectorSet[m].getDisireOutputs()[j] - layer[j].getOut()) * (vectorSet[m].getDisireOutputs()[j] - layer[j].getOut());
                    errorCommon += 0.5 * error[j];
                }


                if (errorCommon < epsThreshold)
                    break;

                // цикл вычисления величин коррекции синаптических весов
                for (int j = 0; j < layer.length; j++)
                {
                    // вычисляем sigma
                    layer[j].calcSigma(vectorSet[m].getDisireOutputs()[j], vectorSet[m].getX());
                    // вычисляем величину изменения синапатических весов wij
                    int n = layer[j].getWeight().length;
                    for (int i = 0; i < n; i++)
                    {
                        deltaWeight[j][i] = - eta * layer[j].getSigma() * vectorSet[m].getX()[i];
                    }
                    layer[j].correctWeights(deltaWeight[j]);
                }

                epochNumber++;
            }

            //Thread.sleep(1);




        complete = true;
    }

    public double[] test(double[] vector)
    {
        // вычисляем выход каждого j-го нейрона
        double[] outVector = new double[layer.length];
        for (int j = 0; j < layer.length; j++)
        {
            layer[j].calcOut(vector);
            outVector[j] = Math.round(layer[j].getOut() * 100.0) / 100.0;
        }
        return outVector;
    }
}
