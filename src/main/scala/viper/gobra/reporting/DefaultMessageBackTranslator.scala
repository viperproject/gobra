package viper.gobra.reporting

import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.silver.reporter.{EntityFailureMessage, EntitySuccessMessage, Message, OverallFailureMessage, OverallSuccessMessage}
import viper.silver.verifier.VerificationResult

class DefaultMessageBackTranslator(backTrackInfo: BackTrackInfo, config: Config) extends MessageBackTranslator {
  override def translate(msg: Message): GobraMessage = {
    defaultTranslate.lift.apply(msg).getOrElse(RawMessage(msg))
  }

  private def defaultTranslate: PartialFunction[Message, GobraMessage] = {
    case m: OverallSuccessMessage => GobraOverallSuccessMessage(m.verifier)
    case m: OverallFailureMessage => GobraOverallFailureMessage(m.verifier, translate(m.result))
    case EntitySuccessMessage(verifier, Source(info), _) => GobraEntitySuccessMessage(verifier, info)
    case EntityFailureMessage(verifier, Source(info), _, result) => GobraEntityFailureMessage(verifier, info, translate(result))
  }

  private def translate(result: VerificationResult): VerifierResult =
    BackTranslator.backTranslate(BackendVerifier.convertVerificationResult(result, backTrackInfo))(config)
}
