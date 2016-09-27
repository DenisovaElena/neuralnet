/**
 * Created by ColdDeath&Dummy on 27.09.2016.
 */
public class Vector
{
    private double[] x;
    private double[] disireOutputs;

    public Vector(double[] x, double[] disireOutputs)
    {
        this.x = x;
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
