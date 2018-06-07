import com.aparapi.internal.kernel.KernelManager

import scala.util.Random
import java.{util => ju}

import com.aparapi.{Kernel, Range}
import com.aparapi.device.Device

object Main {
  val bestDevice: Device = KernelManager.instance.bestDevice
  val range: Range = Range.create(bestDevice.getMaxWorkItemSize()(0), bestDevice.getMaxWorkGroupSize)

  def testKernel(inA: Array[Float], inB: Array[Float]): Array[Float] = {
    val result = new Array[Float](inA.length)
    val kernel = new Kernel() {
      override def run() {
        val i = getGlobalId()
        result(i) = ((inA(i) + inB(i)) / (inA(i) / inB(i))) * ((inA(i) - inB(i)) / (inA(i) * inB(i))) -
          ((inB(i) - inA(i)) * (inB(i) + inA(i))) * ((inB(i) - inA(i)) / (inB(i) * inA(i)))
      }
    }

    kernel.execute(range)
    result
  }

  def main(args: Array[String]): Unit = {
    val bestDevice = KernelManager.instance().bestDevice()
    println(s"Detected best opencl device is a ${bestDevice.getType}")
    println(s"Detected best opencl device: ${bestDevice.getShortDescription}")

    val a = Array.fill(100000000)(Random.nextFloat())
    val b = Array.fill(100000000)(Random.nextFloat())

    val openCLTime = (0 to 10).map { _ =>
      val beforeOCL = System.nanoTime()
//      OCLKernel.testKernel(a, b).length
      testKernel(a, b).length
      val afterOCL = System.nanoTime()
      afterOCL - beforeOCL
    }
      // ignore warmup
      .drop(1)
      .sum / 10

    val cpuTime = (0 to 10).map { _ =>
      val beforeCPU = System.nanoTime()
      OCLKernel.testCPU(a, b).length
      val afterCPU = System.nanoTime()
      afterCPU - beforeCPU
    }
      // ignore warmup
      .drop(1)
      .sum / 10

    // this is still an unreliable way to measure
    println(s"GPU Time: $openCLTime")
    println(s"CPU Time: $cpuTime")
  }
}
