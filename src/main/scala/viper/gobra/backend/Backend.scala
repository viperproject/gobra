package viper.gobra.backend

trait Backend[I, O] {
  def start(): Unit
  def handle(input: I): O
  def stop(): Unit
}
