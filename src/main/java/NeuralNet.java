import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final double eta = 0.001;
    private final double epsThreshold = 0.01;

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

        while (true)
        {
            for (int j = 0; j < layer.length; j++)
            {
                // создаем пустой массив для накопления каждого синпатического веса wij каждого j-го нейрона в слое
                deltaWeight[j] = new double[layer[j].getWeight().length];
                errorCommon = 0.0;
            }
            for (int m = 0; m < vectorSet.length; m++)
            {
                // вычисляем выход каждого j-го нейрона
                for (int j = 0; j < layer.length; j++)
                {
                    layer[j].calcOut(vectorSet[m].getX());
                }

                // создаем пустой массив для хранения ошибки каждого j-го нейрона
                error = new double[layer.length];
                // вычисляем ошибку каждого j-го нейрона
                for (int j = 0; j < layer.length; j++)
                {
                    error[j] = (vectorSet[m].getDisireOutputs()[j] - layer[j].getOut()) * (vectorSet[m].getDisireOutputs()[j] - layer[j].getOut());
                    errorCommon += 0.5 * error[j];
                }

                // цикл вычисления величин коррекции синаптических весов
                for (int j = 0; j < layer.length; j++)
                {
                    // вычисляем sigma
                    layer[j].calcSigma(vectorSet[m].getDisireOutputs()[j], vectorSet[m].getX());
                    // вычисляем величину изменения синапатических весов wij
                    int n = layer[j].getWeight().length;
                    for (int i = 0; i < n; i++)
                    {
                        deltaWeight[j][i] += -eta * layer[j].getSigma() * vectorSet[m].getX()[i];
                    }
                }
            }

            Thread.sleep(1);

            // цикл коррекции синаптических весов
            for (int j = 0; j < layer.length; j++)
            {
                layer[j].correctWeights(deltaWeight[j]);
            }

            if (errorCommon < epsThreshold)
                break;

            epochNumber++;
        } // критерий останова обучения

        complete = true;
    }

    public double[] test(double[] vector)
    {
        // вычисляем выход каждого j-го нейрона
        double[] outVector = new double[layer.length];
        for (int j = 0; j < layer.length; j++)
        {
            layer[j].calcOut(vector);
            outVector[j] = layer[j].getOut();
        }
        return outVector;
    }
}
