package puzzle.core.frontend.parser.matcher.declaration

import puzzle.core.exception.syntaxError
import puzzle.core.frontend.ast.AnnotationCall
import puzzle.core.frontend.ast.DocComment
import puzzle.core.frontend.ast.Modifier
import puzzle.core.frontend.ast.parameter.DeclarationContextSpec
import puzzle.core.frontend.ast.parameter.TypeSpec
import puzzle.core.frontend.ast.type.ErrorsSpec
import puzzle.core.frontend.model.FileContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.parser.ModifierTarget
import puzzle.core.frontend.parser.parser.check

class DeclarationMeta(
	val docComment: DocComment?,
	val annotationCalls: List<AnnotationCall>,
	val typeSpec: TypeSpec?,
	val contextSpec: DeclarationContextSpec?,
	val errorsSpec: ErrorsSpec?,
	val modifiers: List<Modifier>,
)

context(_: FileContext, cursor: PzlTokenCursor)
fun DeclarationMeta.check(target: DeclarationTarget, modifierTarget: ModifierTarget) {
	this.typeSpec?.check(target)
	this.contextSpec?.check(target)
	this.errorsSpec?.check(target)
	this.modifiers.check(modifierTarget)
}

context(_: FileContext, cursor: PzlTokenCursor)
private fun TypeSpec.check(target: DeclarationTarget) {
	if (!target.allowType) {
		syntaxError("${target.label}声明不支持泛型", cursor[this.location.start])
	}
	if (!target.allowTypeVariance) {
		this.parameters.forEach {
			if (it.variance != null) {
				val variance = it.variance
				syntaxError("${target.label}泛型声明不支持使用 '${variance.kind.kind.value}'", variance)
			}
		}
	}
}

context(_: FileContext)
private fun DeclarationContextSpec.check(target: DeclarationTarget) {
	if (!target.allowContext) {
		syntaxError("${target.label}不支持 context 上下文参数", this)
	}
}

context(_: FileContext)
private fun ErrorsSpec.check(target: DeclarationTarget) {
	if (!target.allowErrors) {
		syntaxError("${target.label}不支持 errors 错误类型", this)
	}
}