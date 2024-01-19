package viper.gobra.util

import javax.sound.sampled.{AudioFormat, AudioSystem}

object Sound {

  val SAMPLE_RATE: Float = 8000f

  private class AudioPlayer {

    private val sdl = {
      val af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false)
      val res = AudioSystem.getSourceDataLine(af)
      res.open(af)
      res.start()
      res
    }

    def play(hz: Int, msecs: Int, vol: Double = 1.0): Unit = {
      val bytes = compile(hz, msecs, vol)
      output(bytes)
    }

    private def compile(hz: Int, msecs: Int, vol: Double): Array[Byte] = {
      val buf = Array.ofDim[Byte](msecs * 8)
      for (i <- buf.indices) {
        val angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI
        buf(i) = (Math.sin(angle) * 127.0 * vol).toByte
      }
      buf
    }

    private def output(bytes: Array[Byte]): Unit = {
      for (off <- bytes.indices) {
        sdl.write(bytes, off, 1)
      }
    }

    def close(): Unit = {
      sdl.drain()
      sdl.stop()
      sdl.close()
    }
  }

  def playSong(song: Vector[Int]): Unit = {
    try {
      val player = new AudioPlayer
      for (hz <- song) {
        player.play(hz,100)
        Thread.sleep(100)
      }
      player.close()
    } catch {
      case _: Throwable =>
    }
  }

  def playSuccess(): Unit = {
    // playSong(Vector(400,200,800,1200,2000,1200,200,800,1200,2000,1200,5000))
    playSong(Vector(2000, 1200, 5000))
  }

  def playFailure(): Unit = {
    // playSong(Vector(400,200,800,1200,2000,1200,200,800,1200,2000,1200,5000))
    playSong(Vector(400, 400, 400))
  }
}
