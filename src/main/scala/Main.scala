import com.aparapi.internal.kernel.KernelManager

import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val bestDevice = KernelManager.instance().bestDevice()
    println(s"Detected best opencl device is a ${bestDevice.getType}")
    println(s"Detected best opencl device: ${bestDevice.getShortDescription}")

    val a = Array.fill(100000000)(Random.nextFloat())
    val b = Array.fill(100000000)(Random.nextFloat())

    val openCLTime = (0 to 10).map { _ =>
      val beforeOCL = System.nanoTime()
      OCLKernel.testKernel(a, b).length
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
