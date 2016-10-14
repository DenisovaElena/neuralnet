/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class Vector
{
    private double[] x;
    private double[] disireOutputs;

    public Vector(double[] x, double[] disireOutputs)
    {
        this.x = new double[x.length + 1];
        this.x[0] = 1;
        for (int i = 1; i < this.x.length; i++)
        {
            this.x[i] = x[i - 1];
        }
        this.disireOutputs = disireOutputs;
    }

    public double[] getX()
    {
        return x;
    }

    public double[] getDisireOutputs()
    {
        return disireOutputs;
    }
}
