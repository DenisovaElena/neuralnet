import java.util.ArrayList;
import java.util.List;

/**
 * Created by MakhrovSS on 27.09.2016.
 */
public class Layer
{
    private List neuron = new ArrayList<Neuron>();

    public Layer()
    {
    }

    public void add(Neuron neuron)
    {
        this.neuron.add(neuron);
    }

    public List getNeurons()
    {
        return neuron;
    }
}
