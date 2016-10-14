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
    private final double eta = 0.5;

    public NeuralNet(int inputVectorSize, int outputNeuronsCount)
    {
        this.inputVectorSize = inputVectorSize;
        layer = new Neuron[outputNeuronsCount];
        for (int j = 0; j < outputNeuronsCount; j++)
        {
            layer[j] = new Neuron(inputVectorSize);
        }
        error = new double[layer.length];
    }

    public double[] getError() {
        return error;
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
        // задаем коэффициент скорости обучения
        epochNumber = 0;
        do
        {
            for (int m = 0; m < vectorSet.length; m++) {
                // вычисляем выход каждого j-го нейрона
                for (int j = 0; j < layer.length; j++) {
                    layer[j].calcOut(vectorSet[m].getX());
                }

                // создаем пустой массив для хранения ошибки каждого j-го нейрона
                error = new double[layer.length];
                // вычисляем ошибку каждого j-го нейрона
                double sumError = 0;
                for (int j = 0; j < layer.length; j++) {
                    error[j] = vectorSet[m].getDisireOutputs()[j] - layer[j].getOut();
                    sumError += error[j];
                }

                Thread.sleep(10);
                // цикл коррекции синаптических весов
                for (int j = 0; j < layer.length; j++) {
                    // создаем пустой массив для хранения величины изменения каждого синпатического веса wij
                    double[] deltaWeight = new double[layer[j].getWeight().length];
                    // вычисляем величину изменения синапатических весов wij
                    int n = layer[j].getWeight().length;
                    for (int i = 0; i < n; i++) {
                        deltaWeight[i] += eta * error[j] * vectorSet[m].getX()[i];
                    }
                    // корректируем синпатические веса
                    layer[j].correctWeights(deltaWeight);
                }

            }
            epochNumber++;
        } // критерий останова обучения
        while (epochNumber <= 10);
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
