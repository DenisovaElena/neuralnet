/**
 * Created by ColdDeath&Dummy on 27.09.2016.
 */
public class NeuralNet
{
    private int inputVectorSize;
    private Neuron[] layer;

    public NeuralNet(int inputVectorSize, int outputNeuronsCount)
    {
        this.inputVectorSize = inputVectorSize;
        layer = new Neuron[outputNeuronsCount];
        for (int j = 0; j < outputNeuronsCount; j++)
        {
            layer[j] = new Neuron(inputVectorSize);
        }
    }

    public void train(Vector[] vectorSet)
    {
        // задаем коэффициент скорости обучения
        double eta = 0.5;
        for (int m = 0; m < vectorSet.length; m++)
        {
            // вычисляем выход каждого j-го нейрона
            for (int j = 0; j < layer.length; j++)
            {
                layer[j].calcOut(vectorSet[m].getX());
            }

            // создаем пустой массив для хранения ошибки каждого j-го нейрона
            double[] error = new double[layer.length];
            // вычисляем ошибку каждого j-го нейрона
            double sumError = 0;
            for (int j = 0; j < layer.length; j++)
            {
                error[j] = vectorSet[m].getDisireOutputs()[j] - layer[j].getOut();
                sumError += error[j];
            }

            // цикл коррекции синаптических весов
            for (int j = 0; j < layer.length; j++)
            {
                // создаем пустой массив для хранения величины изменения каждого синпатического веса wij
                double[] deltaWeight = new double[layer[j].getWeight().length];
                // вычисляем величину изменения синпатических весов wij
                for (int i = 0; i < layer[j].getWeight().length; i++)
                {
                    deltaWeight[i] += eta * error[j] * vectorSet[m].getX()[i];
                }
                // корректируем синпатические веса
                layer[j].correctWeights(deltaWeight);
            }

            // критерий останова обучения
            if (sumError == 0)
                break;
        }
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
