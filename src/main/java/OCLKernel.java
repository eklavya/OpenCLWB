import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;

// arbitrary complex expression calculation
public class OCLKernel {
    static final Device bestDevice = KernelManager.instance().bestDevice();
    static final Range range = Range.create(bestDevice.getMaxWorkItemSize()[0], bestDevice.getMaxWorkGroupSize());

    static float[] testKernel(float[] inA, float[] inB) {
        final float result[] = new float[inA.length];
        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                result[i] = ((inA[i] + inB[i]) / (inA[i] / inB[i])) * ((inA[i] - inB[i]) / (inA[i] * inB[i])) -
                        ((inB[i] - inA[i]) * (inB[i] + inA[i])) * ((inB[i] - inA[i]) / (inB[i] * inA[i]));
            }
        };

        kernel.execute(range);
        return result;
    }

    static float[] testCPU(float[] inA, float[] inB) {
        final float result[] = new float[inA.length];
        for (int i = 0; i < inA.length; i++) {
            result[i] = ((inA[i] + inB[i]) / (inA[i] / inB[i])) * ((inA[i] - inB[i]) / (inA[i] * inB[i])) -
                    ((inB[i] - inA[i]) * (inB[i] + inA[i])) * ((inB[i] - inA[i]) / (inB[i] * inA[i]));
        }
        return result;
    }
}
