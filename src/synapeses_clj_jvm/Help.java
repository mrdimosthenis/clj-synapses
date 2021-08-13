package synapeses_clj_jvm;

import synapses.lib.Fun$package.Fun$;
import synapses.model.net_elems.activation.Activation;

public class Help {

    public static final Activation SIGMOID = Fun$.MODULE$.sigmoid();

    public static final Activation IDENTITY = Fun$.MODULE$.identity();

    public static final Activation TANH = Fun$.MODULE$.tanh();

    public static final Activation LEAKY_RE_LU = Fun$.MODULE$.leakyReLU();

}
